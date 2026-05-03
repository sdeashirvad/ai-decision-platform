package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for anomaly calculation result")
public class AnomalyCalculationResponseDto {

    @Schema(description = "Number of new anomalies inserted", example = "5")
    private int anomaliesInserted;

    @Schema(description = "Number of existing anomalies updated", example = "3")
    private int anomaliesUpdated;

    public AnomalyCalculationResponseDto(int anomaliesInserted, int anomaliesUpdated) {
        this.anomaliesInserted = anomaliesInserted;
        this.anomaliesUpdated = anomaliesUpdated;
    }

    public int getAnomaliesInserted() {
        return anomaliesInserted;
    }

    public void setAnomaliesInserted(int anomaliesInserted) {
        this.anomaliesInserted = anomaliesInserted;
    }

    public int getAnomaliesUpdated() {
        return anomaliesUpdated;
    }

    public void setAnomaliesUpdated(int anomaliesUpdated) {
        this.anomaliesUpdated = anomaliesUpdated;
    }
}
