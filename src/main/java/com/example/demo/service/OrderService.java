package com.example.demo.service;

import com.example.demo.component.GetRedis;
import com.example.demo.entity.CustomerEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.PromotionEntity;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.request.CreateOrderRequest;
import com.example.demo.model.response.GetOrderResponse;
import com.example.demo.model.response.GetOrdersResponse;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GetRedis redis;

    public void createOrder(CreateOrderRequest request) {
        String customerEmail = request.getCustomerEmail();
        Long promotionId = request.getPromotionId();

        CustomerEntity customer = customerRepository
                .findByEmail(customerEmail)
                .orElseThrow(() -> {
                    log.warn("Customer not found email={}",  customerEmail);
                    return new ResourceNotFoundException(ErrorCode.CustomerNotFound);
                });

        List<PromotionEntity> promotions = redis.getPromotions();

        Optional<PromotionEntity> optPromotion = promotions
                .stream()
                .filter(p -> p.getId().equals(promotionId))
                .findFirst();

        PromotionEntity promotion = null;
        promotion = optPromotion.orElseGet(() -> promotionRepository
                .findById(promotionId)
                .orElseThrow(() -> {
                    log.warn(
                            "Promotion not found id={}",
                            promotionId
                    );
                    return new ResourceNotFoundException(ErrorCode.PromotionNotFound);
                }));

        OrderEntity createdOrder = request.toOrderEntity(customer, promotion);
        createdOrder = orderRepository.save(createdOrder);
        log.debug("Created order id={}", createdOrder.getId());
    }

    public GetOrderResponse getOrder(Long id) {
        OrderEntity order = orderRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found id={}", id);
                    return new ResourceNotFoundException(ErrorCode.OrderNotFound);
                });

        log.debug("Found order id={}", order.getId());

        var response = new GetOrderResponse();
        response.setOrderId(order.getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderDate(order.getOrderDatetime());

        response.setCustomerEmail(order.getCustomer().getEmail());
        response.setCustomerMobileNo(order.getCustomer().getMobileNo());

        response.setPromotionName(order.getPromotion().getName());
        response.setPromotionExpiration(order.getPromotion().getExpiredDate());

        return response;
    }

    public List<GetOrdersResponse> getOrders(Long customerId) {
        CustomerEntity customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Customer not found id={}",  customerId);
                    return new ResourceNotFoundException(ErrorCode.CustomerNotFound);
                });

        List<OrderEntity> orders = orderRepository.findByCustomerId(customer.getId());
        if (orders.isEmpty()) throw new ResourceNotFoundException(ErrorCode.OrderNotFound);

        return orders
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private GetOrdersResponse toResponse(OrderEntity orderEntity) {
        var response = new GetOrdersResponse();
        response.setOrderId(orderEntity.getId());
        response.setTotalPrice(orderEntity.getTotalPrice());
        response.setOrderDate(orderEntity.getOrderDatetime());
        return response;
    }

}
