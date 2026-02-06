package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Data Transfer Object for Anomaly Record")
public class AnomalyRecordDto {
    @Schema(description = "Unique identifier of the anomaly")
    private UUID id;
    
    @Schema(description = "Date of the anomaly", example = "2023-10-25")
    private LocalDate date;
    
    @Schema(description = "Trading desk name", example = "EquityDerivatives")
    private String desk;
    
    @Schema(description = "Deviation from the mean PnL", example = "150000.00")
    private BigDecimal deviation;
    
    @Schema(description = "Severity of the anomaly", example = "HIGH")
    private String severity;

    public AnomalyRecordDto() {
    }

    public AnomalyRecordDto(UUID id, LocalDate date, String desk, BigDecimal deviation, String severity) {
        this.id = id;
        this.date = date;
        this.desk = desk;
        this.deviation = deviation;
        this.severity = severity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public BigDecimal getDeviation() {
        return deviation;
    }

    public void setDeviation(BigDecimal deviation) {
        this.deviation = deviation;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
