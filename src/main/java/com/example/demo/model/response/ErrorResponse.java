package com.example.demo.model.response;

public record ErrorResponse(
        String code,
        String message
) {
}
