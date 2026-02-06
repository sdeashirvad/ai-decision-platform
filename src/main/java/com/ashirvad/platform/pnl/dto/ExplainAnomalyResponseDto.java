package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO containing the AI-driven explanation of an anomaly")
public class ExplainAnomalyResponseDto {
    @Schema(description = "The reason for the anomaly", example = "Significant deviation from historical average detected.")
    private String reason;
    
    @Schema(description = "The severity of the anomaly", example = "HIGH")
    private String severity;
    
    @Schema(description = "Suggested action to take", example = "Investigate desk trading activity immediately.")
    private String suggestedAction;

    public ExplainAnomalyResponseDto() {
    }

    public ExplainAnomalyResponseDto(String reason, String severity, String suggestedAction) {
        this.reason = reason;
        this.severity = severity;
        this.suggestedAction = suggestedAction;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }
}
