package com.example.demo.controller;

import com.example.demo.model.request.CreateOrderRequest;
import com.example.demo.model.response.GetOrderResponse;
import com.example.demo.model.response.GetOrdersResponse;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable @Positive @NotNull Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/list-order/{customerId}")
    public ResponseEntity<List<GetOrdersResponse>> getOrders(@PathVariable @Positive @NotNull Long customerId) {
        return ResponseEntity.ok(orderService.getOrders(customerId));
    }

}
