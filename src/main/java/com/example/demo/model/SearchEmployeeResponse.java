package com.example.demo.model;

import java.math.BigDecimal;

public record SearchEmployeeResponse(
        String employeeName,
        BigDecimal salary,
        String department
) {
}
