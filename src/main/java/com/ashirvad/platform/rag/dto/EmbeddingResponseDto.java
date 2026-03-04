package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class EmbeddingResponseDto {

    @Schema(description = "The dimension of the embedding vector", example = "768")
    private int dimension;

    @Schema(description = "A preview of the first 5 values of the embedding vector")
    private List<Float> vectorPreview;

    public EmbeddingResponseDto(int dimension, List<Float> vectorPreview) {
        this.dimension = dimension;
        this.vectorPreview = vectorPreview;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public List<Float> getVectorPreview() {
        return vectorPreview;
    }

    public void setVectorPreview(List<Float> vectorPreview) {
        this.vectorPreview = vectorPreview;
    }
}
