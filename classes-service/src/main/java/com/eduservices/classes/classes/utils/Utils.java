package com.eduservices.classes.utils;

import java.time.Instant;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class Utils {


    public static String generateRandomNumber(int length) {
        // Use current time in milliseconds as seed
        long seed = Instant.now().toEpochMilli();
        // Create a RandomGenerator using the new Java 17 API
        RandomGenerator random = RandomGeneratorFactory.of("L64X128MixRandom").create(seed);
        // Generate a number with the specified length
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10)); // Digits 0–9
        }
        return result.toString();
    }

}
