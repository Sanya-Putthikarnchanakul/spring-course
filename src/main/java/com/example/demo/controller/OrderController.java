package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable String id) {
        log.info("Fetch order with id=[{}]", id);

        if (id.equals("404")) {
            log.warn("Order not found id=[{}]", id);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(MessageFormat.format("order-{0}", id));
    }

}
