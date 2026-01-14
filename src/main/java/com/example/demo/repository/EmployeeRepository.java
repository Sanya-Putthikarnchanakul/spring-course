package com.example.demo.repository;

import com.example.demo.entity.EmployeeEntity;
import com.example.demo.model.SearchEmployeeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByEmployeeNameContaining(String searchingName);

    @Query("""
        select new com.example.demo.model.SearchEmployeeResponse(
            emp.employeeName, 
            emp.salary, 
            dpm.departmentName
        )
        from EmployeeEntity emp
        join emp.department dpm
        where emp.employeeName like %:searchingName%
    """)
    List<SearchEmployeeResponse> projectionQuery(@Param("searchingName") String searchingName);

}
