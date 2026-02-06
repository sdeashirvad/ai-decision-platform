package com.ashirvad.platform.etl.controller;

import com.ashirvad.platform.etl.dto.EtlDiagnosisRequestDto;
import com.ashirvad.platform.etl.dto.EtlDiagnosisResponseDto;
import com.ashirvad.platform.etl.service.EtlDiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/etl")
@Tag(name = "ETL APIs", description = "Operations for ETL diagnosis and monitoring")
public class EtlController {

    private final EtlDiagnosisService etlDiagnosisService;

    public EtlController(EtlDiagnosisService etlDiagnosisService) {
        this.etlDiagnosisService = etlDiagnosisService;
    }

    @Operation(summary = "Diagnose ETL Error",
               description = "Diagnose an Airflow ETL error using AI to determine root cause and suggested actions.",
               tags = {"ETL APIs", "GenAI Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully diagnosed error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EtlDiagnosisResponseDto.class)))
    })
    @PostMapping("/diagnose")
    public ResponseEntity<EtlDiagnosisResponseDto> diagnoseError(@RequestBody EtlDiagnosisRequestDto request) {
        EtlDiagnosisResponseDto response = etlDiagnosisService.diagnose(request);
        return ResponseEntity.ok(response);
    }
}
