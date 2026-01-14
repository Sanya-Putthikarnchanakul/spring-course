package com.example.demo.service;

import com.example.demo.entity.EmployeeEntity;
import com.example.demo.model.PageMetadata;
import com.example.demo.model.PagedResponse;
import com.example.demo.model.SearchEmployeeResponse;
import com.example.demo.model.SearchEmployeeV1AllResponse;
import com.example.demo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<SearchEmployeeResponse> searchEmployeeV1(String name) {
        StopWatch sw = new StopWatch();
        sw.start();

        List<EmployeeEntity> employees = employeeRepository.findByEmployeeNameContaining(name);

        List<SearchEmployeeResponse> response = employees
                .stream()
                .map(emp -> new SearchEmployeeResponse(emp.getEmployeeName(), emp.getSalary(), emp.getDepartment().getDepartmentName()))
                .toList();

        sw.stop();

        writeLog(sw.getTotalTimeMillis());

        return response;
    }

    public List<SearchEmployeeResponse> searchEmployeeV2(String name) {
        StopWatch sw = new StopWatch();
        sw.start();

        List<SearchEmployeeResponse> response = employeeRepository.projectionQuery(name);

        sw.stop();

        writeLog(sw.getTotalTimeMillis());

        return response;
    }

    @Transactional(readOnly = true)
    public PagedResponse<SearchEmployeeV1AllResponse> searchEmployeeV1All(Pageable pageable) {
        Pageable safePageable = PageRequest.of(
                pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), 20),
                pageable.getSort()
        );

        Page<EmployeeEntity> pages = employeeRepository.findAll(safePageable);

        List<SearchEmployeeV1AllResponse> data = pages
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        PageMetadata metadata = new PageMetadata(
                pages.getNumber(),
                pages.getSize(),
                pages.getTotalElements(),
                pages.getTotalPages(),
                pages.hasNext(),
                pages.hasPrevious()
        );

        return new PagedResponse<>(data, metadata);
    }

    private void writeLog(long ms) {
        log.info("Execution time: {} ms", ms);
    }

    private SearchEmployeeV1AllResponse toResponse(EmployeeEntity entity) {
        return new SearchEmployeeV1AllResponse(
                entity.getId(),
                entity.getEmployeeName(),
                entity.getSalary()
        );
    }

}
