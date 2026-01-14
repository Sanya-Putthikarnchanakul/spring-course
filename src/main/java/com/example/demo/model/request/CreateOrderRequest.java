package com.example.demo.model.request;

import com.example.demo.entity.CustomerEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.PromotionEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateOrderRequest {

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    @NotNull
    private String customerEmail;

    @NotNull
    @Positive
    private Long promotionId;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public OrderEntity toOrderEntity(CustomerEntity customer, PromotionEntity promotion) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setTotalPrice(totalPrice);
        orderEntity.setOrderDatetime(Instant.now());
        orderEntity.setCustomer(customer);
        orderEntity.setPromotion(promotion);

        // สามารถเก็บเป็น OffsetDateTime ได้
        // orderEntity.setOrderDatetime(OffsetDateTime.parse(orderDatetime));
        // orderEntity.setPromotionExpired(Instant.parse("2026-01-20T23:59:59.999Z"));

        return orderEntity;
    }
}
