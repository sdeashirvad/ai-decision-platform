package com.ashirvad.platform.rag.service;

import com.ashirvad.platform.rag.dto.RagChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RagStoreServiceImpl implements RagStoreService {

    private final ConcurrentHashMap<String, List<RagChunk>> store = new ConcurrentHashMap<>();

    @Override
    public void createTenant(String tenant) {
        store.putIfAbsent(tenant, new ArrayList<>());
    }

    @Override
    public List<String> getTenants() {
        return new ArrayList<>(store.keySet());
    }

    @Override
    public void addChunks(String tenant, List<RagChunk> chunks) {
        store.computeIfPresent(tenant, (key, value) -> {
            value.addAll(chunks);
            return value;
        });
    }

    @Override
    public List<RagChunk> getChunks(String tenant) {
        return store.getOrDefault(tenant, new ArrayList<>());
    }

    @Override
    public Map<String, Integer> debugSummary() {
        return store.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
    }
}
