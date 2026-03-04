package com.ashirvad.platform.ai;

import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public String run(SystemPromptType promptType, Map<String, String> variables, String userPrompt) {
        String systemPrompt = PromptTemplates.get(promptType);
        
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                String placeholder = "{{" + entry.getKey() + "}}";
                systemPrompt = systemPrompt.replace(placeholder, entry.getValue());
            }
        }

        return geminiClient.generate(systemPrompt, userPrompt);
    }
}
