package com.ashirvad.platform.rag.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    @Value("${GEMINI_API_KEY1}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public float[] embedText(String text) {
        // Corrected URL with the full model name
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key=" + apiKey;

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(Collections.singletonMap("text", text)));

        Map<String, Object> requestBody = new HashMap<>();
        // Corrected model name in the body
        requestBody.put("model", "models/gemini-embedding-001");
        requestBody.put("content", content);
        // Added taskType as per best practices
        requestBody.put("taskType", "RETRIEVAL_DOCUMENT");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return parseEmbedding(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    private float[] parseEmbedding(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            // The embedding is nested under a top-level "embedding" key
            JsonNode valuesNode = root.path("embedding").path("values");
            
            if (valuesNode.isMissingNode() || !valuesNode.isArray()) {
                // Log the actual response for debugging if the structure is unexpected
                throw new RuntimeException("Invalid response format: 'embedding.values' not found or not an array. Response: " + jsonResponse);
            }

            float[] embedding = new float[valuesNode.size()];
            for (int i = 0; i < valuesNode.size(); i++) {
                embedding[i] = (float) valuesNode.get(i).asDouble();
            }
            return embedding;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse embedding response", e);
        }
    }
}
