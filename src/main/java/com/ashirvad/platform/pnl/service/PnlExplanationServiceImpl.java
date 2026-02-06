package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import com.ashirvad.platform.pnl.dto.ExplainAnomalyRequestDto;
import com.ashirvad.platform.pnl.dto.ExplainAnomalyResponseDto;
import com.ashirvad.platform.pnl.model.AnomalyRecord;
import com.ashirvad.platform.pnl.model.PnlRecord;
import com.ashirvad.platform.pnl.repository.AnomalyRecordRepository;
import com.ashirvad.platform.pnl.repository.PnlRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PnlExplanationServiceImpl implements PnlExplanationService {

    private final AiService aiService;
    private final AnomalyRecordRepository anomalyRecordRepository;
    private final PnlRecordRepository pnlRecordRepository;
    private final ObjectMapper objectMapper;

    public PnlExplanationServiceImpl(AiService aiService, AnomalyRecordRepository anomalyRecordRepository, PnlRecordRepository pnlRecordRepository) {
        this.aiService = aiService;
        this.anomalyRecordRepository = anomalyRecordRepository;
        this.pnlRecordRepository = pnlRecordRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ExplainAnomalyResponseDto explainAnomaly(ExplainAnomalyRequestDto request) {
        Optional<AnomalyRecord> anomalyOpt = anomalyRecordRepository.findById(request.getAnomalyId());
        if (anomalyOpt.isEmpty()) {
            return new ExplainAnomalyResponseDto("Anomaly not found", "UNKNOWN", "None");
        }

        AnomalyRecord anomaly = anomalyOpt.get();
        List<PnlRecord> pnlRecords = pnlRecordRepository.findByDeskAndDate(anomaly.getDesk(), anomaly.getDate());

        BigDecimal currentPnl = pnlRecords.stream()
                .map(PnlRecord::getPnlAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate endDate = anomaly.getDate().minusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        BigDecimal avgPnl = pnlRecordRepository.findAveragePnlForDesk(anomaly.getDesk(), startDate, endDate);
        if (avgPnl == null) {
            avgPnl = BigDecimal.ZERO;
        }

        String userPrompt = String.format(
                "Analyze this PnL anomaly:\n" +
                        "Desk: %s\n" +
                        "Date: %s\n" +
                        "Current PnL: %s\n" +
                        "7-Day Avg PnL: %s\n" +
                        "Deviation: %s\n" +
                        "Severity: %s\n\n" +
                        "Return strictly valid JSON with fields: reason, severity, suggestedAction. Do not include markdown formatting.",
                anomaly.getDesk(),
                anomaly.getDate(),
                currentPnl,
                avgPnl,
                anomaly.getDeviation(),
                anomaly.getSeverity()
        );

        String aiResponse = aiService.run(SystemPromptType.PNL_EXPLANATION, userPrompt);

        return parseAiResponse(aiResponse);
    }

    private ExplainAnomalyResponseDto parseAiResponse(String aiResponse) {
        try {
            // Clean up potential Markdown code blocks
            String jsonContent = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
            Map<String, String> responseMap = objectMapper.readValue(jsonContent, Map.class);

            return new ExplainAnomalyResponseDto(
                    responseMap.getOrDefault("reason", "Could not determine reason"),
                    responseMap.getOrDefault("severity", "UNKNOWN"),
                    responseMap.getOrDefault("suggestedAction", "Investigate manually")
            );
        } catch (Exception e) {
            System.err.println("Failed to parse AI response: " + aiResponse);
            return new ExplainAnomalyResponseDto(
                    "AI explanation unavailable (Parse Error). Raw response: " + aiResponse,
                    "UNKNOWN",
                    "Check logs"
            );
        }
    }
}