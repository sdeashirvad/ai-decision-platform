package com.ashirvad.platform.ai;

public interface AiService {
    String run(SystemPromptType promptType, String userPrompt);
}
