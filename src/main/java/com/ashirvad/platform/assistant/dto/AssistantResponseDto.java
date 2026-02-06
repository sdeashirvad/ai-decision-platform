package com.ashirvad.platform.assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO from the AI Assistant")
public class AssistantResponseDto {

    @Schema(description = "The persona used for the assistant's response", example = "RISK_MANAGER")
    private Persona persona;

    @Schema(description = "The summary of the assistant's findings", example = "Based on the recent data, the top anomalies are...")
    private String summary;

    @Schema(description = "The recommended action from the assistant", example = "Investigate the trades from the 'Equity Derivatives' desk.")
    private String recommendedAction;

    public AssistantResponseDto() {
    }

    public AssistantResponseDto(Persona persona, String summary, String recommendedAction) {
        this.persona = persona;
        this.summary = summary;
        this.recommendedAction = recommendedAction;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}
