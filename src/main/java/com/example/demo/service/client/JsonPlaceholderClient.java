package com.example.demo.service.client;

import com.example.demo.model.client.TodoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "JsonPlaceholderClient", url = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceholderClient {

    @GetMapping(path = "/todos")
    List<TodoResponse> getTodos();

}
