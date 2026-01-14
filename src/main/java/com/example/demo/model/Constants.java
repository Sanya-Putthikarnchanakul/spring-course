package com.example.demo.model;

public enum Constants {
    PromotionRedisKey("promotions");

    private final String key;

    Constants(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
