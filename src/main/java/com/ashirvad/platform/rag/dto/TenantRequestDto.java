package com.ashirvad.platform.rag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TenantRequestDto {

    @Schema(description = "The name of the tenant to create", example = "acme")
    private String tenant;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
