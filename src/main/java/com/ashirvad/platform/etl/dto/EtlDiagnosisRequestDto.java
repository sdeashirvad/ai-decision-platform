package com.ashirvad.platform.etl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class EtlDiagnosisRequestDto {

    @Schema(description = "The ID of the DAG", example = "pnl_calculation_dag")
    private String dagId;

    @Schema(description = "The ID of the task", example = "calculate_pnl")
    private String taskId;

    @Schema(description = "The error message from the task", example = "division by zero")
    private String error;

    @Schema(description = "The URL to the logs of the task", example = "http://localhost:8080/logs/pnl_calculation_dag/calculate_pnl/1")
    private String logUrl;

    public String getDagId() {
        return dagId;
    }

    public void setDagId(String dagId) {
        this.dagId = dagId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }
}
