package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for anomaly calculation result")
public class AnomalyCalculationResponseDto {
    @Schema(description = "Number of new anomalies created", example = "5")
    private long anomaliesCreated;

    public AnomalyCalculationResponseDto(long anomaliesCreated) {
        this.anomaliesCreated = anomaliesCreated;
    }

    public long getAnomaliesCreated() {
        return anomaliesCreated;
    }

    public void setAnomaliesCreated(long anomaliesCreated) {
        this.anomaliesCreated = anomaliesCreated;
    }
}
