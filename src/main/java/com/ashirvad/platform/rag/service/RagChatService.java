package com.ashirvad.platform.rag.service;

import com.ashirvad.platform.rag.dto.RagChatRequestDto;
import com.ashirvad.platform.rag.dto.RagChatResponseDto;

public interface RagChatService {
    RagChatResponseDto chat(RagChatRequestDto request);
}
