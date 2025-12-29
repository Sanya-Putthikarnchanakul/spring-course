package com.example.demo.controller;

import com.example.demo.model.SaveOneRequest;
import com.example.demo.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/thread")
public class ThreadController {

    private static final Logger log = LoggerFactory.getLogger(ThreadController.class);

    @Autowired
    private ThreadService threadService;

    @PostMapping("/save-one")
    public ResponseEntity<?> saveOne(@RequestBody SaveOneRequest request) throws IOException {
        String userId = threadService.saveOne(request);

        var response = new HashMap<String, String>();
        response.put("userId", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/save-all")
    public ResponseEntity<?> saveAll(@RequestBody Map<String, Integer> request) {
        Integer number = request.get("number");
        log.info("Start");

        threadService.saveAll(number);

        log.info("End");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
