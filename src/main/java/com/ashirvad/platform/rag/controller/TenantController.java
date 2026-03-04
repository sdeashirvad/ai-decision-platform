package com.ashirvad.platform.rag.controller;

import com.ashirvad.platform.rag.dto.RagChatRequestDto;
import com.ashirvad.platform.rag.dto.RagChatResponseDto;
import com.ashirvad.platform.rag.dto.SeedResponseDto;
import com.ashirvad.platform.rag.dto.TenantRequestDto;
import com.ashirvad.platform.rag.service.RagChatService;
import com.ashirvad.platform.rag.service.RagStoreService;
import com.ashirvad.platform.rag.service.SeedingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
@Tag(name = "RAG APIs", description = "APIs for Retrieval-Augmented Generation")
public class TenantController {

    private final RagStoreService ragStoreService;
    private final SeedingService seedingService;
    private final RagChatService ragChatService;

    public TenantController(RagStoreService ragStoreService, SeedingService seedingService, RagChatService ragChatService) {
        this.ragStoreService = ragStoreService;
        this.seedingService = seedingService;
        this.ragChatService = ragChatService;
    }

    @Operation(summary = "Create a new tenant", description = "Creates a new tenant for storing RAG chunks.")
    @PostMapping("/tenants")
    public ResponseEntity<String> createTenant(@RequestBody TenantRequestDto request) {
        ragStoreService.createTenant(request.getTenant());
        return ResponseEntity.ok("Tenant created: " + request.getTenant());
    }

    @Operation(summary = "List all tenants", description = "Returns a list of all existing tenants.")
    @GetMapping("/tenants")
    public ResponseEntity<List<String>> getTenants() {
        return ResponseEntity.ok(ragStoreService.getTenants());
    }

    @Operation(summary = "Get debug summary", description = "Returns a map of tenants and their chunk counts.")
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Integer>> getDebugSummary() {
        return ResponseEntity.ok(ragStoreService.debugSummary());
    }

    @Operation(summary = "Seed a tenant's knowledge base", description = "Upload a .txt file to seed a tenant's knowledge base.")
    @PostMapping(value = "/tenants/{tenant}/seed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SeedResponseDto> seedTenant(
            @PathVariable String tenant,
            @Parameter(description = "Text file to upload", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) throws IOException {
        int chunks = seedingService.seedTenant(tenant, file);
        return ResponseEntity.ok(new SeedResponseDto("indexed", tenant, chunks));
    }

    @Operation(summary = "Chat with a tenant's knowledge base", description = "Ask a question to a tenant's knowledge base.")
    @PostMapping("/chat")
    public ResponseEntity<RagChatResponseDto> chat(@RequestBody RagChatRequestDto request) {
        return ResponseEntity.ok(ragChatService.chat(request));
    }
}
