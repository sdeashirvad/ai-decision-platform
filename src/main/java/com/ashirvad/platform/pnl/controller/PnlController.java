package com.ashirvad.platform.pnl.controller;

import com.ashirvad.platform.pnl.dto.AnomalyCalculationResponseDto;
import com.ashirvad.platform.pnl.dto.AnomalyRecordDto;
import com.ashirvad.platform.pnl.dto.ExplainAnomalyRequestDto;
import com.ashirvad.platform.pnl.dto.ExplainAnomalyResponseDto;
import com.ashirvad.platform.pnl.dto.PnlRecordDto;
import com.ashirvad.platform.pnl.service.AnomalyCalculationService;
import com.ashirvad.platform.pnl.service.PnlExplanationService;
import com.ashirvad.platform.pnl.service.PnlIngestionService;
import com.ashirvad.platform.pnl.service.PnlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pnl")
@Tag(name = "PnL APIs", description = "API for PnL records, anomalies, and related operations")
public class PnlController {

    private final PnlService pnlService;
    private final PnlIngestionService pnlIngestionService;
    private final PnlExplanationService pnlExplanationService;
    private final AnomalyCalculationService anomalyCalculationService;

    public PnlController(PnlService pnlService, PnlIngestionService pnlIngestionService,
                         PnlExplanationService pnlExplanationService, AnomalyCalculationService anomalyCalculationService) {
        this.pnlService = pnlService;
        this.pnlIngestionService = pnlIngestionService;
        this.pnlExplanationService = pnlExplanationService;
        this.anomalyCalculationService = anomalyCalculationService;
    }

    @Operation(summary = "Get PnL records",
               description = "Retrieve a list of PnL records, with optional filters for desk, fromDate, and toDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved PnL records",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PnlRecordDto.class)))
    })
    @GetMapping("/records")
    public List<PnlRecordDto> getPnlRecords(
            @Parameter(description = "Filter by desk name") @RequestParam(required = false) String desk,
            @Parameter(description = "Filter records from this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Filter records up to this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return pnlService.getPnlRecords(desk, fromDate, toDate);
    }

    @Operation(summary = "Delete PnL records",
            description = "Delete PnL records based on optional filters: id, desk, fromDate, and toDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted PnL records")
    })
    @DeleteMapping("/records")
    public ResponseEntity<Void> deletePnlRecords(
            @Parameter(description = "ID of the record to delete") @RequestParam(required = false) UUID id,
            @Parameter(description = "Filter by desk name") @RequestParam(required = false) String desk,
            @Parameter(description = "Filter records from this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Filter records up to this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        pnlService.deletePnlRecords(id, fromDate, toDate, desk);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create PnL record",
            description = "Create a new PnL record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created PnL record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PnlRecordDto.class)))
    })
    @PostMapping("/records")
    public ResponseEntity<PnlRecordDto> createPnlRecord(@RequestBody PnlRecordDto pnlRecordDto) {
        return ResponseEntity.ok(pnlService.createPnlRecord(pnlRecordDto));
    }

    @Operation(summary = "Get PnL anomalies",
               description = "Retrieve a list of PnL anomalies, with optional filters for desk, fromDate, and toDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved PnL anomalies",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnomalyRecordDto.class)))
    })
    @GetMapping("/anomalies")
    public List<AnomalyRecordDto> getAnomalies(
            @Parameter(description = "Filter by desk name") @RequestParam(required = false) String desk,
            @Parameter(description = "Filter anomalies from this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Filter anomalies up to this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return pnlService.getAnomalies(desk, fromDate, toDate);
    }

    @Operation(summary = "Delete PnL anomalies",
            description = "Delete PnL anomalies based on optional filters: id, desk, fromDate, and toDate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted PnL anomalies")
    })
    @DeleteMapping("/anomalies")
    public ResponseEntity<Void> deleteAnomalies(
            @Parameter(description = "ID of the anomaly to delete") @RequestParam(required = false) UUID id,
            @Parameter(description = "Filter by desk name") @RequestParam(required = false) String desk,
            @Parameter(description = "Filter anomalies from this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Filter anomalies up to this date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        pnlService.deleteAnomalies(id, fromDate, toDate, desk);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create PnL anomaly",
            description = "Create a new PnL anomaly record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created PnL anomaly",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnomalyRecordDto.class)))
    })
    @PostMapping("/anomalies")
    public ResponseEntity<AnomalyRecordDto> createAnomaly(@RequestBody AnomalyRecordDto anomalyRecordDto) {
        return ResponseEntity.ok(pnlService.createAnomalyRecord(anomalyRecordDto));
    }

    @Operation(summary = "Upload PnL data from CSV",
               description = "Uploads a CSV file containing PnL records for ingestion into the system. Expected columns: date,desk,product,pnl_amount.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully ingested PnL records",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, e.g., empty file"),
            @ApiResponse(responseCode = "500", description = "Internal server error during ingestion")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Integer>> uploadPnlData(
            @Parameter(description = "CSV file to upload", required = true) @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Map<String, Integer> result = pnlIngestionService.ingestPnlData(file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Explain PnL anomaly using AI",
               description = "Requests an AI-driven explanation for a PnL anomaly based on provided details.",
               tags = {"PnL APIs", "GenAI Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved anomaly explanation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExplainAnomalyResponseDto.class)))
    })
    @PostMapping("/anomalies/explain")
    public ResponseEntity<ExplainAnomalyResponseDto> explainAnomaly(@RequestBody ExplainAnomalyRequestDto request) {
        ExplainAnomalyResponseDto response = pnlExplanationService.explainAnomaly(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Calculate and persist PnL anomalies",
               description = "Fetches existing PnL records, calculates anomalies based on deviation from mean PnL per desk, and persists them.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated and persisted anomalies",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AnomalyCalculationResponseDto.class)))
    })
    @PostMapping("/anomalies/calculate")
    public ResponseEntity<AnomalyCalculationResponseDto> calculateAnomalies(
            @Parameter(description = "Optional: Calculate anomalies only for a specific desk") @RequestParam(required = false) String desk) {
        AnomalyCalculationResponseDto response = anomalyCalculationService.calculateAnomalies(desk);
        return ResponseEntity.ok(response);
    }
}
