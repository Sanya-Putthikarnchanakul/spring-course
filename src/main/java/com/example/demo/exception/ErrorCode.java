package com.example.demo.exception;

public enum ErrorCode {
    CustomerNotFound("B001", "Customer not found."),
    PromotionNotFound("B002", "Promotion not found."),
    OrderNotFound("B003", "Order not found.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
