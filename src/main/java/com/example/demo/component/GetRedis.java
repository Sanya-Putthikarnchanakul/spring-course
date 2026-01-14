package com.example.demo.component;

import com.example.demo.entity.PromotionEntity;
import com.example.demo.model.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class GetRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<PromotionEntity> getPromotions() {
        String redisKey = Constants.PromotionRedisKey.getKey();
        String promotionsJsonString = stringRedisTemplate.opsForValue().get(redisKey);
        return objectMapper.readValue(promotionsJsonString, new TypeReference<>() {});
    }

}
