package com.ashirvad.platform.pnl.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface PnlIngestionService {
    Map<String, Integer> ingestPnlData(MultipartFile file) throws IOException;
}
