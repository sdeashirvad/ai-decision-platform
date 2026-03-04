package com.ashirvad.platform.ai;

import java.util.Map;

public interface AiService {
    String run(SystemPromptType promptType, String userPrompt);
    String run(SystemPromptType promptType, Map<String, String> variables, String userPrompt);
}
