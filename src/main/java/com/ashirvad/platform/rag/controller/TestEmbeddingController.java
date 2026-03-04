package com.ashirvad.platform.rag.controller;

import com.ashirvad.platform.rag.dto.EmbeddingRequestDto;
import com.ashirvad.platform.rag.dto.EmbeddingResponseDto;
import com.ashirvad.platform.rag.service.EmbeddingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rag/embed")
@Tag(name = "RAG APIs", description = "APIs for Retrieval-Augmented Generation")
public class TestEmbeddingController {

    private final EmbeddingService embeddingService;

    public TestEmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Operation(summary = "Test text embedding",
               description = "Generates an embedding for the given text and returns its dimension and a preview of the first 5 values.")
    @PostMapping("/test")
    public ResponseEntity<EmbeddingResponseDto> testEmbedding(@RequestBody EmbeddingRequestDto request) {
        float[] embedding = embeddingService.embedText(request.getText());

        List<Float> preview = new ArrayList<>();
        for (int i = 0; i < Math.min(embedding.length, 5); i++) {
            preview.add(embedding[i]);
        }

        EmbeddingResponseDto response = new EmbeddingResponseDto(
                embedding.length,
                preview
        );

        return ResponseEntity.ok(response);
    }
}
