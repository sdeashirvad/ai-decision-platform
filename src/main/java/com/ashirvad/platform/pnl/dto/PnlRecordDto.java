package com.ashirvad.platform.pnl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Data Transfer Object for PnL Record")
public class PnlRecordDto {
    @Schema(description = "Unique identifier of the record")
    private UUID id;
    
    @Schema(description = "Date of the PnL record", example = "2023-10-25")
    private LocalDate date;
    
    @Schema(description = "Trading desk name", example = "EquityDerivatives")
    private String desk;
    
    @Schema(description = "Product name", example = "Options")
    private String product;
    
    @Schema(description = "Profit and Loss amount", example = "-5000.00")
    private BigDecimal pnlAmount;

    public PnlRecordDto() {
    }

    public PnlRecordDto(UUID id, LocalDate date, String desk, String product, BigDecimal pnlAmount) {
        this.id = id;
        this.date = date;
        this.desk = desk;
        this.product = product;
        this.pnlAmount = pnlAmount;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getPnlAmount() {
        return pnlAmount;
    }

    public void setPnlAmount(BigDecimal pnlAmount) {
        this.pnlAmount = pnlAmount;
    }
}
