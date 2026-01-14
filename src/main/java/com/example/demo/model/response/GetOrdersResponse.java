package com.example.demo.model.response;

import java.math.BigDecimal;
import java.time.Instant;

public class GetOrdersResponse {

    private Long orderId;
    private BigDecimal totalPrice;
    private Instant orderDate;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Instant getOrderDate() {
        return orderDate;
    }
}
