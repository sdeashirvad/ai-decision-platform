package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class SeedResponseDto {

    @Schema(description = "The status of the seeding operation", example = "indexed")
    private String status;

    @Schema(description = "The tenant that was seeded", example = "acme")
    private String tenant;

    @Schema(description = "The number of chunks created and stored", example = "14")
    private int chunks;

    public SeedResponseDto(String status, String tenant, int chunks) {
        this.status = status;
        this.tenant = tenant;
        this.chunks = chunks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }
}
