package com.example.demo.service;

import com.example.demo.model.client.TodoResponse;
import com.example.demo.service.client.JsonPlaceholderClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private static final Logger LOG = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private JsonPlaceholderClient jsonPlaceholderClient;

    public void getTodos() {

        LOG.info("getTodos => start");

        try {
            List<TodoResponse> todos =  jsonPlaceholderClient.getTodos();
            LOG.debug("Successfully retrieved {} todos", todos.size());
        } catch (FeignException fe) {
            LOG.error("API todos error", fe);
        }

        LOG.info("getTodos => end");

    }

}
