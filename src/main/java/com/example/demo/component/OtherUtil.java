package com.example.demo.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtherUtil {

    public int randomAge() {
        int min = 18;
        int max = 80;

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}
