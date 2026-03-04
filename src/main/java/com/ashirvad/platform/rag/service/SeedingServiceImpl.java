package com.ashirvad.platform.rag.service;

import com.ashirvad.platform.rag.dto.RagChunk;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SeedingServiceImpl implements SeedingService {

    private static final int CHUNK_SIZE = 400;
    private static final int OVERLAP = 70;
    private static final long MAX_FILE_SIZE = 100 * 1024; // 100KB

    private final RagStoreService ragStoreService;
    private final EmbeddingService embeddingService;

    public SeedingServiceImpl(RagStoreService ragStoreService, EmbeddingService embeddingService) {
        this.ragStoreService = ragStoreService;
        this.embeddingService = embeddingService;
    }

    @Override
    public int seedTenant(String tenant, MultipartFile file) throws IOException {
        validateFile(file);
        validateTenant(tenant);

        String content = readFileContent(file);
        List<String> textChunks = chunkText(content);
        List<RagChunk> ragChunks = new ArrayList<>();

        for (String text : textChunks) {
            float[] embedding = embeddingService.embedText(text);
            RagChunk chunk = new RagChunk(UUID.randomUUID().toString(), text, embedding, null);
            ragChunks.add(chunk);
        }

        ragStoreService.addChunks(tenant, ragChunks);
        return ragChunks.size();
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 100KB");
        }
        if (!file.getOriginalFilename().toLowerCase().endsWith(".txt")) {
            throw new IllegalArgumentException("Only .txt files are supported");
        }
    }

    private void validateTenant(String tenant) {
        if (!ragStoreService.getTenants().contains(tenant)) {
            throw new IllegalArgumentException("Tenant '" + tenant + "' does not exist");
        }
    }

    private String readFileContent(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private List<String> chunkText(String text) {
        List<String> chunks = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return chunks;
        }

        int length = text.length();
        int start = 0;
        while (start < length) {
            int end = Math.min(start + CHUNK_SIZE, length);
            chunks.add(text.substring(start, end));
            start += (CHUNK_SIZE - OVERLAP);
        }
        return chunks;
    }
}
