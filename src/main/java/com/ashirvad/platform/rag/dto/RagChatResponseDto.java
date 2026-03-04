package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class RagChatResponseDto {

    @Schema(description = "The AI's response")
    private String answer;

    public RagChatResponseDto(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
