package com.ashirvad.platform.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiClient {

    @Value("${GEMINI_API_KEY1}")
    private String apiKey1;

    @Value("${GEMINI_API_KEY2}")
    private String apiKey2;

    @Value("${GEMINI_API_KEY3}")
    private String apiKey3;

    private List<String> apiKeys;
    private int currentKeyIndex = 0;
    private int requestCount = 0;
    private static final int ROTATION_THRESHOLD = 3;
    private final RestTemplate restTemplate = new RestTemplate();

    private void init() {
        apiKeys = List.of(apiKey1, apiKey2, apiKey3);
    }

    public String generate(String systemInstruction, String userPrompt) {
        if (apiKeys == null) {
            init();
        }
        String apiKey = apiKeys.get(currentKeyIndex);
        String response = callGemini(userPrompt, systemInstruction, apiKey, "gemini-2.5-flash");

        if (response.contains("model_not_found")) {
            System.out.println("model_not_found Falling back to gemini-2.5-flash...");
            response = callGemini(userPrompt, systemInstruction, apiKey, "gemini-2.5-flash");
        }
        return response;
    }

    private String callGemini(String userPrompt, String systemInstruction, String apiKey, String model) {
        System.out.println("Using model: " + model + " with API key index #" + (currentKeyIndex + 1));

        try {
            if (++requestCount >= ROTATION_THRESHOLD) {
                rotateKey();
            }

            String url = constructUrl(model, apiKey);
            HttpEntity<Map<String, Object>> request = constructRequest(systemInstruction, userPrompt);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            return parseResponse(response.getBody());

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Model not found: " + model + ". Attempting fallback...");
            if (!model.equals("gemini-2.5-flash")) {
                return callGemini(userPrompt, systemInstruction, apiKey, "gemini-2.5-flash");
            }
            return "Error: Model not found.";

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.out.println("Rate limit hit on key #" + (currentKeyIndex + 1));
            rotateKey();
            return callGemini(userPrompt, systemInstruction, apiKeys.get(currentKeyIndex), model);

        } catch (HttpClientErrorException.BadRequest badRequest) {
            System.out.println("Bad request: " + badRequest.getResponseBodyAsString());
            return "Invalid request payload.";

        } catch (Exception e) {
            System.out.println("Error calling Gemini API: " + e.getMessage());
            return "Something went wrong while processing your request.";
        }
    }

    private String constructUrl(String model, String apiKey) {
        return "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
    }

    private HttpEntity<Map<String, Object>> constructRequest(String systemInstruction, String userPrompt) {
        Map<String, Object> body = new HashMap<>();
        
        if (systemInstruction != null && !systemInstruction.isEmpty()) {
            Map<String, Object> systemInstructionMap = new HashMap<>();
            systemInstructionMap.put("parts", Collections.singletonList(
                    Collections.singletonMap("text", systemInstruction)
            ));
            body.put("system_instruction", systemInstructionMap);
        }

        Map<String, Object> userContent = new HashMap<>();
        userContent.put("role", "user");
        userContent.put("parts", Collections.singletonList(
                Collections.singletonMap("text", userPrompt)
        ));
        body.put("contents", Collections.singletonList(userContent));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(body, headers);
    }

    private String parseResponse(Map<String, Object> responseBody) {
        if (responseBody != null) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return parts.get(0).get("text").toString();
                }
            }
        }
        return "Gemini returned no valid content.";
    }

    private void rotateKey() {
        currentKeyIndex = (currentKeyIndex + 1) % apiKeys.size();
        requestCount = 0;
        System.out.println("Rotated to API key #" + (currentKeyIndex + 1));
    }
}