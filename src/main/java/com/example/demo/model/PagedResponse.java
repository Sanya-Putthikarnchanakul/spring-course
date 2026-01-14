package com.example.demo.model;

import java.util.List;

public record PagedResponse<T>(
        List<T> data,
        PageMetadata metadata
) {
}
