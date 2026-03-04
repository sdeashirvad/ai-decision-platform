package com.ashirvad.platform.rag.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SeedingService {
    int seedTenant(String tenant, MultipartFile file) throws IOException;
}
