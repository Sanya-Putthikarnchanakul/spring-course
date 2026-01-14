package com.example.demo.controller;

import com.example.demo.model.PagedResponse;
import com.example.demo.model.SearchEmployeeResponse;
import com.example.demo.model.SearchEmployeeV1AllResponse;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/v1")
    public ResponseEntity<?> searchEmployeeV1(@RequestParam String name) {
        List<SearchEmployeeResponse> response = employeeService.searchEmployeeV1(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v2")
    public ResponseEntity<?> searchEmployeeV2(@RequestParam String name) {
        List<SearchEmployeeResponse> response = employeeService.searchEmployeeV2(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/all")
    public ResponseEntity<PagedResponse<SearchEmployeeV1AllResponse>> searchEmployeeV1All(
            @PageableDefault(
                    page = 0,
                    size = 20,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(employeeService.searchEmployeeV1All(pageable));
    }

}
