package com.ashirvad.platform.rag.service;

import com.ashirvad.platform.rag.dto.RagChunk;

import java.util.List;
import java.util.Map;

public interface RagStoreService {
    void createTenant(String tenant);
    List<String> getTenants();
    void addChunks(String tenant, List<RagChunk> chunks);
    List<RagChunk> getChunks(String tenant);
    Map<String, Integer> debugSummary();
}
