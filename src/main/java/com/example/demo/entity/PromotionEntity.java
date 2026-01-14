package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "PROMOTIONS")
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    @Column(name = "EXPIRED_DATE", columnDefinition = "DATETIME2(3)")
    private Instant expiredDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getExpiredDate() {
        return expiredDate;
    }
}
