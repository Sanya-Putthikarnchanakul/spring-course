package com.example.demo.service.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange(url = "https://echo.zuplo.io")
public interface EchoClient {

    @PostExchange
    Map<?, ?> echo(@RequestBody Map<String, Object> request);

}
