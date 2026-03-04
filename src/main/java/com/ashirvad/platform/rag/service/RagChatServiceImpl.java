package com.ashirvad.platform.rag.service;

import com.ashirvad.platform.ai.AiService;
import com.ashirvad.platform.ai.SystemPromptType;
import com.ashirvad.platform.rag.dto.RagChatRequestDto;
import com.ashirvad.platform.rag.dto.RagChatResponseDto;
import com.ashirvad.platform.rag.dto.RagChunk;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagChatServiceImpl implements RagChatService {

    private final RagStoreService ragStoreService;
    private final EmbeddingService embeddingService;
    private final AiService aiService;

    public RagChatServiceImpl(RagStoreService ragStoreService, EmbeddingService embeddingService, AiService aiService) {
        this.ragStoreService = ragStoreService;
        this.embeddingService = embeddingService;
        this.aiService = aiService;
    }

    @Override
    public RagChatResponseDto chat(RagChatRequestDto request) {
        String tenant = request.getTenant();
        if (!ragStoreService.getTenants().contains(tenant)) {
            throw new IllegalArgumentException("Tenant '" + tenant + "' does not exist");
        }

        float[] queryEmbedding = embeddingService.embedText(request.getMessage());
        List<RagChunk> chunks = ragStoreService.getChunks(tenant);

        List<String> topChunks = chunks.stream()
                .sorted(Comparator.comparingDouble(chunk -> -cosineSimilarity(queryEmbedding, chunk.getEmbedding())))
                .limit(3)
                .map(RagChunk::getText)
                .collect(Collectors.toList());

        String context = String.join("\n\n", topChunks);
        String mode = request.getMode() != null ? request.getMode().toUpperCase() : "STRICT";

        SystemPromptType promptType;
        Map<String, String> variables = new HashMap<>();
        variables.put("context", context);

        if ("BALANCED".equals(mode)) {
            promptType = SystemPromptType.RAG_TENANT_CHAT_BALANCED;
            variables.put("userMessage", request.getMessage());
        } else {
            promptType = SystemPromptType.RAG_TENANT_CHAT;
        }

        String answer = aiService.run(
                promptType,
                variables,
                request.getMessage()
        );

        return new RagChatResponseDto(answer);
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
