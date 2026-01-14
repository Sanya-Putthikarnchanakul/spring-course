package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ORDERS")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOTAL_PRICE", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    // สามารถเก็บเป็น OffsetDateTime ได้
    // @Column(name = "ORDER_DATETIME", nullable = false, columnDefinition = "DATETIMEOFFSET(3)")
    // private OffsetDateTime orderDatetime;

    @Column(name = "ORDER_DATETIME", nullable = false, columnDefinition = "DATETIME2(3)")
    private Instant orderDatetime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID")
    private PromotionEntity promotion;

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Instant getOrderDatetime() {
        return orderDatetime;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDatetime(Instant orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public void setPromotion(PromotionEntity promotion) {
        this.promotion = promotion;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public PromotionEntity getPromotion() {
        return promotion;
    }
}
