package com.ashirvad.platform.assistant.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AiAssistantResponseDto {

    private final String summary;
    private final String recommendedAction;

    @JsonCreator
    public AiAssistantResponseDto(
            @JsonProperty("summary") String summary,
            @JsonProperty("recommendedAction") String recommendedAction) {
        this.summary = summary;
        this.recommendedAction = recommendedAction;
    }

    public String getSummary() {
        return summary;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }
}
