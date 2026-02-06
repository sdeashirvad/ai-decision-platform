package com.ashirvad.platform.etl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class EtlDiagnosisResponseDto {

    @Schema(description = "The root cause of the ETL failure", example = "The database connection was lost.")
    private String rootCause;

    @Schema(description = "The severity of the ETL failure", example = "HIGH")
    private Severity severity;

    @Schema(description = "The suggested action to resolve the ETL failure", example = "Restart the database and check the connection pool.")
    private String suggestedAction;

    @Schema(description = "Whether the task is safe to retry", example = "false")
    private boolean retrySafe;

    public EtlDiagnosisResponseDto() {
    }

    public EtlDiagnosisResponseDto(String rootCause, Severity severity, String suggestedAction, boolean retrySafe) {
        this.rootCause = rootCause;
        this.severity = severity;
        this.suggestedAction = suggestedAction;
        this.retrySafe = retrySafe;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public boolean isRetrySafe() {
        return retrySafe;
    }

    public void setRetrySafe(boolean retrySafe) {
        this.retrySafe = retrySafe;
    }
}
