package com.example.demo.model.response;

import java.time.Instant;

public class GetOrderResponse extends GetOrdersResponse {
    private String customerEmail;
    private String customerMobileNo;
    private String promotionName;
    private Instant promotionExpiration;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Instant getPromotionExpiration() {
        return promotionExpiration;
    }

    public void setPromotionExpiration(Instant promotionExpiration) {
        this.promotionExpiration = promotionExpiration;
    }
}
