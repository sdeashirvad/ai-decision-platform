package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class RagChatRequestDto {

    @Schema(description = "The tenant to query against", example = "acme")
    private String tenant;

    @Schema(description = "The user's message", example = "What does this company do?")
    private String message;

    @Schema(description = "The grounding mode: STRICT (default) or BALANCED", example = "STRICT", allowableValues = {"STRICT", "BALANCED"})
    private String mode;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
