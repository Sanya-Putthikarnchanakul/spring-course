package com.example.demo.model.client;

public record TodoResponse(
    Long userId,
    Long id,
    String title,
    Boolean completed
) {
}
