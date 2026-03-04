package com.ashirvad.platform.rag.dto;

import java.util.Map;

public class RagChunk {
    private String id;
    private String text;
    private float[] embedding;
    private Map<String, Object> metadata;

    public RagChunk() {
    }

    public RagChunk(String id, String text, float[] embedding, Map<String, Object> metadata) {
        this.id = id;
        this.text = text;
        this.embedding = embedding;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
