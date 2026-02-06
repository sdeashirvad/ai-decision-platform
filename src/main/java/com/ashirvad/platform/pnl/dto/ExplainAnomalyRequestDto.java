package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Request DTO for explaining an anomaly")
public class ExplainAnomalyRequestDto {
    @Schema(description = "ID of the anomaly to explain", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID anomalyId;

    public ExplainAnomalyRequestDto() {
    }

    public ExplainAnomalyRequestDto(UUID anomalyId) {
        this.anomalyId = anomalyId;
    }

    public UUID getAnomalyId() {
        return anomalyId;
    }

    public void setAnomalyId(UUID anomalyId) {
        this.anomalyId = anomalyId;
    }
}
