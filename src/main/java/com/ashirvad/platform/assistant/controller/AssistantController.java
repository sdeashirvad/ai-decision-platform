package com.ashirvad.platform.assistant.controller;

import com.ashirvad.platform.assistant.dto.AssistantRequestDto;
import com.ashirvad.platform.assistant.dto.AssistantResponseDto;
import com.ashirvad.platform.assistant.service.AssistantService;
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
@RequestMapping("/api/assistant")
@Tag(name = "Assistant APIs", description = "AI Assistant for querying platform data")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Operation(summary = "Query the AI Assistant",
               description = "Ask a question to the AI assistant with a specific persona (RISK_MANAGER, DATA_ENGINEER, PRODUCT_OWNER).",
               tags = {"Assistant APIs", "GenAI Endpoints"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved answer",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AssistantResponseDto.class)))
    })
    @PostMapping("/query")
    public ResponseEntity<AssistantResponseDto> queryAssistant(@RequestBody AssistantRequestDto request) {
        AssistantResponseDto response = assistantService.queryAssistant(request);
        return ResponseEntity.ok(response);
    }
}
