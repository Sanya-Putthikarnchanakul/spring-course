package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CUSTOMERS")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "MOBILE_NO", nullable = false, length = 20, unique = true)
    private String mobileNo;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
