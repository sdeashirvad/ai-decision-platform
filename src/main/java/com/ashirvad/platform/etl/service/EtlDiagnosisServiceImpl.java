package com.ashirvad.platform.etl.service;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import com.ashirvad.platform.etl.dto.AiEtlDiagnosisResponseDto;
import com.ashirvad.platform.etl.dto.EtlDiagnosisRequestDto;
import com.ashirvad.platform.etl.dto.EtlDiagnosisResponseDto;
import com.ashirvad.platform.etl.dto.Severity;
import com.ashirvad.platform.util.JsonParsingService;
import org.springframework.stereotype.Service;

@Service
public class EtlDiagnosisServiceImpl implements EtlDiagnosisService {

    private final AiService aiService;
    private final JsonParsingService jsonParsingService;

    public EtlDiagnosisServiceImpl(AiService aiService, JsonParsingService jsonParsingService) {
        this.aiService = aiService;
        this.jsonParsingService = jsonParsingService;
    }

    @Override
    public EtlDiagnosisResponseDto diagnose(EtlDiagnosisRequestDto request) {
        String userPrompt = buildUserPrompt(request);
        String aiResponse = aiService.run(SystemPromptType.ETL_DIAGNOSIS, userPrompt);
        AiEtlDiagnosisResponseDto parsedResponse = jsonParsingService.parse(aiResponse, AiEtlDiagnosisResponseDto.class);

        if (parsedResponse == null) {
            return new EtlDiagnosisResponseDto("Unable to parse AI diagnosis", Severity.UNKNOWN, "Check logs for more details", false);
        }

        return new EtlDiagnosisResponseDto(
                parsedResponse.getRootCause(),
                parsedResponse.getSeverity(),
                parsedResponse.getSuggestedAction(),
                parsedResponse.isRetrySafe()
        );
    }

    private String buildUserPrompt(EtlDiagnosisRequestDto request) {
        return "DAG ID: " + request.getDagId() + "\n" +
               "Task ID: " + request.getTaskId() + "\n" +
               "Error: " + request.getError() + "\n" +
               "Log URL: " + request.getLogUrl();
    }
}
