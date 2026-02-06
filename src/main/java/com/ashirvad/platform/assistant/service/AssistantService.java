package com.ashirvad.platform.assistant.service;

import com.ashirvad.platform.assistant.dto.AssistantRequestDto;
import com.ashirvad.platform.assistant.dto.AssistantResponseDto;

public interface AssistantService {
    AssistantResponseDto queryAssistant(AssistantRequestDto request);
}
