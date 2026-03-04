package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class EmbeddingRequestDto {

    @Schema(description = "The text to embed", example = "What is the meaning of life?")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
