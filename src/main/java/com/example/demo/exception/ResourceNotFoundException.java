package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String code;

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
