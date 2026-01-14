package com.example.demo.controller;

import com.example.demo.service.CustomerService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/dump")
    public ResponseEntity<Void> dumpCustomer(@RequestParam @Positive Integer number) {
        customerService.dumpCustomer(number);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
