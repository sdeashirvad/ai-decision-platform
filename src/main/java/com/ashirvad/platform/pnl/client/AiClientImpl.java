package com.ashirvad.platform.pnl.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiClientImpl implements AiClient {

    @Value("${AI_API_KEY:dummy-key}")
    private String apiKey;

    @Override
    public String generateExplanation(String prompt) {
        // In a real implementation, this would call an external AI service using the apiKey.
        // For now, we return a stubbed response based on the prompt.
        return "Reason: Significant deviation from historical average detected. Severity: HIGH. Suggested Action: Investigate desk trading activity immediately.";
    }
}
