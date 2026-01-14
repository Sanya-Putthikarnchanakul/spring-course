package com.example.demo.model;

import java.math.BigDecimal;

public record SearchEmployeeV1AllResponse(
        long id,
        String employeeName,
        BigDecimal salary
) {
}
