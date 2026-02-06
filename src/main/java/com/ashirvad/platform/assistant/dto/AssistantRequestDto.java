package com.ashirvad.platform.assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for querying the AI Assistant")
public class AssistantRequestDto {
    @Schema(description = "The persona to use for the assistant", example = "RISK_MANAGER", allowableValues = {"RISK_MANAGER", "DATA_ENGINEER", "PRODUCT_OWNER"})
    private Persona persona;
    
    @Schema(description = "The question to ask the assistant", example = "What are the top 3 anomalies this week?")
    private String question;

    public AssistantRequestDto() {
    }

    public AssistantRequestDto(Persona persona, String question) {
        this.persona = persona;
        this.question = question;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
