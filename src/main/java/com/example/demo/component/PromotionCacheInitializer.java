package com.example.demo.component;

import com.example.demo.entity.PromotionEntity;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.model.Constants;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Component
public class PromotionCacheInitializer implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void run(String @NonNull ... args) {
        String redisKey = Constants.PromotionRedisKey.getKey();

        String promotionsJsonString = stringRedisTemplate.opsForValue().get(redisKey);

        if (promotionsJsonString == null) {
            List<PromotionEntity> promotions = promotionRepository.findAll();

            if (promotions.isEmpty()) return;

            promotionsJsonString = objectMapper.writeValueAsString(promotions);
            stringRedisTemplate.opsForValue().set(redisKey, promotionsJsonString, Duration.ofHours(1));
        }
    }

}
