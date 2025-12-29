package com.example.demo.service;

import com.example.demo.model.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class DbService {

    private static final Logger log = LoggerFactory.getLogger(DbService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file.path}")
    private String filepathString;

    @Async
    public void save(UserEntity userEntity) throws IOException {
        String jsonString = objectMapper.writeValueAsString(userEntity) + System.lineSeparator();
        Files.write(Paths.get(filepathString), jsonString.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    public List<UserEntity> findAll() throws IOException {
        Path filepath = Paths.get(filepathString);
        String jsonString = Files.readString(filepath);

        if (jsonString.isEmpty()) return new ArrayList<>();

        return objectMapper.readValue(jsonString, new TypeReference<List<UserEntity>>() {});
    }

    public void saveTry(UserEntity userEntity) throws IOException {
        Path filepath = Paths.get(filepathString);
        String jsonString = Files.readString(filepath);
        var users = new ArrayList<UserEntity>();

        if (jsonString.isEmpty()) {
            users.add(userEntity);
            jsonString = objectMapper.writeValueAsString(users);
            Files.write(filepath, jsonString.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            return;
        }

        users = objectMapper.readValue(jsonString, new TypeReference<>() {});
        users.add(userEntity);
        jsonString = objectMapper.writeValueAsString(users);
        Files.write(filepath, jsonString.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

}
