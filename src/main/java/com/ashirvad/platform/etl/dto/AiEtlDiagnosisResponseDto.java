package com.ashirvad.platform.etl.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AiEtlDiagnosisResponseDto {

    private final String rootCause;
    private final Severity severity;
    private final String suggestedAction;
    private final boolean retrySafe;

    @JsonCreator
    public AiEtlDiagnosisResponseDto(
            @JsonProperty("rootCause") String rootCause,
            @JsonProperty("severity") Severity severity,
            @JsonProperty("suggestedAction") String suggestedAction,
            @JsonProperty("retrySafe") boolean retrySafe) {
        this.rootCause = rootCause;
        this.severity = severity;
        this.suggestedAction = suggestedAction;
        this.retrySafe = retrySafe;
    }

    public String getRootCause() {
        return rootCause;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public boolean isRetrySafe() {
        return retrySafe;
    }
}
