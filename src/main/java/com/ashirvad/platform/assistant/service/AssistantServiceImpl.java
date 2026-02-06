package com.ashirvad.platform.assistant.service;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import com.ashirvad.platform.assistant.dto.AiAssistantResponseDto;
import com.ashirvad.platform.assistant.dto.AssistantRequestDto;
import com.ashirvad.platform.assistant.dto.AssistantResponseDto;
import com.ashirvad.platform.assistant.dto.Persona;
import com.ashirvad.platform.pnl.model.AnomalyRecord;
import com.ashirvad.platform.pnl.repository.AnomalyRecordRepository;
import com.ashirvad.platform.util.JsonParsingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssistantServiceImpl implements AssistantService {

    private final AnomalyRecordRepository anomalyRecordRepository;
    private final AiService aiService;
    private final JsonParsingService jsonParsingService;

    public AssistantServiceImpl(
            AnomalyRecordRepository anomalyRecordRepository,
            AiService aiService,
            JsonParsingService jsonParsingService) {
        this.anomalyRecordRepository = anomalyRecordRepository;
        this.aiService = aiService;
        this.jsonParsingService = jsonParsingService;
    }

    @Override
    public AssistantResponseDto queryAssistant(AssistantRequestDto request) {
        List<AnomalyRecord> recentAnomalies = anomalyRecordRepository.findTop5ByOrderByDateDesc();
        String anomalyContext = recentAnomalies.stream()
                .map(a -> String.format("[Date: %s, Desk: %s, Deviation: %s, Severity: %s]",
                        a.getDate(), a.getDesk(), a.getDeviation(), a.getSeverity()))
                .collect(Collectors.joining("; "));

        String userPrompt = buildUserPrompt(request, anomalyContext);
        SystemPromptType promptType = getPromptType(request.getPersona());

        String aiResponse = aiService.run(promptType, userPrompt);
        AiAssistantResponseDto parsedResponse = jsonParsingService.parse(aiResponse, AiAssistantResponseDto.class);

        if (parsedResponse == null) {
            return new AssistantResponseDto(request.getPersona(), "I am having trouble processing your request. Please try again later.", "No action recommended.");
        }

        return new AssistantResponseDto(request.getPersona(), parsedResponse.getSummary(), parsedResponse.getRecommendedAction());
    }

    private String buildUserPrompt(AssistantRequestDto request, String anomalyContext) {
        return "Context: Recent anomalies: " + anomalyContext + "\n" +
               "Question: " + request.getQuestion() + "\n";
    }

    private SystemPromptType getPromptType(Persona persona) {
        switch (persona) {
            case RISK_MANAGER:
                return SystemPromptType.ROLE_ASSISTANT_RISK_MANAGER;
            case DATA_ENGINEER:
                return SystemPromptType.ROLE_ASSISTANT_DATA_ENGINEER;
            case PRODUCT_OWNER:
                return SystemPromptType.ROLE_ASSISTANT_PRODUCT_OWNER;
            default:
                throw new IllegalArgumentException("Invalid persona: " + persona);
        }
    }
}
