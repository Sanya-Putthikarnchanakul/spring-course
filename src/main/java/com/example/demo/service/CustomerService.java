package com.example.demo.service;

import com.example.demo.entity.CustomerEntity;
import com.example.demo.repository.CustomerRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Faker faker;

    public void dumpCustomer(Integer number) {
        List<CustomerEntity> customers = faker
                .collection(() -> {
                    var customer = new CustomerEntity();
                    customer.setEmail(faker.internet().emailAddress());
                    customer.setMobileNo(faker.phoneNumber().phoneNumber());
                    return customer;
                })
                .len(10)
                .generate();

        customers = customerRepository.saveAll(customers);
        log.debug("[CustomerService.dumpCustomer] => length={}", customers.size());
    }

}
