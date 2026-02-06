package com.ashirvad.platform.ai;

import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final GeminiClient geminiClient;

    public AiServiceImpl(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    @Override
    public String run(SystemPromptType promptType, String userPrompt) {
        String systemPrompt = PromptTemplates.get(promptType);
        return geminiClient.generate(systemPrompt, userPrompt);
    }
}
