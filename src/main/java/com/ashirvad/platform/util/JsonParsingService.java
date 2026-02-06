package com.ashirvad.platform.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JsonParsingService {

    private static final Logger logger = LoggerFactory.getLogger(JsonParsingService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T parse(String json, Class<T> clazz) {
        try {
            String cleanedJson = json.replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleanedJson, clazz);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON: {}", json, e);
            return null;
        }
    }
}
