package com.example.demo.controller;

import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

// https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html
@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody HashMap<String, Object> request) {
        String name = (String) request.get("name");
        Long newAddedId = personService.createPerson(name);

        var response = new HashMap<String, Object>();
        response.put("addedId", newAddedId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        HashMap<String, Object> response = personService.getPersonById(id);

        if (response == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(response);
    }

}
