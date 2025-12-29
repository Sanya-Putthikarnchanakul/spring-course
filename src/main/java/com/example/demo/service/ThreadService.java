package com.example.demo.service;

import com.example.demo.component.OtherUtil;
import com.example.demo.model.SaveOneRequest;
import com.example.demo.model.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@Service
public class ThreadService {

    private static final Logger log = LoggerFactory.getLogger(ThreadService.class);

    @Autowired
    private DbService dbService;

    @Autowired
    private OtherUtil otherUtil;

    public void saveAll(int number) {
        try {
            for (int i = 1; i <= number; i++) {
                var user = new UserEntity(
                        UUID.randomUUID().toString(),
                        MessageFormat.format("Fname{0} Lname{1}", i, i),
                        otherUtil.randomAge()
                );
                dbService.save(user);
            }
        } catch (Exception e) {
            log.error("Error", e);
        }
    }

    public String saveOne(SaveOneRequest request) throws IOException {
        String userId = UUID.randomUUID().toString();

        var addUser = new UserEntity(
                userId,
                request.name(),
                request.age()
        );

        dbService.saveTry(addUser);

        return  userId;
    }

}
