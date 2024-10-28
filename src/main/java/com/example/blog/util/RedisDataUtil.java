package com.example.blog.util;

import java.util.LinkedHashMap;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisDataUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisDataUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

      // Utility method to convert LinkedHashMap to an object of the desired class
      public static <T> Optional<T> convertRedisDataToObject(Object redisData, Class<T> clazz) {
        if (redisData instanceof LinkedHashMap) {
            T object = objectMapper.convertValue(redisData, clazz);
            try {
                // Convert the object to JSON for logging purposes
                String json = objectMapper.writeValueAsString(object);
                log.info("redisData (JSON): {}", json);
            } catch (JsonProcessingException e) {
                log.error("Error converting object to JSON", e);
            }
            return Optional.of(object);
        }
        return Optional.empty(); // Input is not a LinkedHashMap
    }
    
    // Utility method to build Redis key
    public static String buildRedisKey(String entityType, String id) {
        return entityType + "::" + id;
    }
}
