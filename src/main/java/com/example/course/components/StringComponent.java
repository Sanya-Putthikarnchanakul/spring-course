package com.example.course.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StringComponent {

    @Value("${validation.product.regex}")
    private String regex;

    public boolean isProductNameValid(String productName) {
        return productName.matches(regex);
    }

}
