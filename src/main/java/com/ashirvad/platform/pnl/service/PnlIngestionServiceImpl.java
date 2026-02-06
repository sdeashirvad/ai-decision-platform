package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.model.PnlRecord;
import com.ashirvad.platform.pnl.repository.PnlRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PnlIngestionServiceImpl implements PnlIngestionService {

    private final PnlRecordRepository pnlRecordRepository;

    public PnlIngestionServiceImpl(PnlRecordRepository pnlRecordRepository) {
        this.pnlRecordRepository = pnlRecordRepository;
    }

    @Override
    public Map<String, Integer> ingestPnlData(MultipartFile file) throws IOException {
        List<PnlRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    try {
                        PnlRecord record = new PnlRecord();
                        record.setDate(LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE));
                        record.setDesk(parts[1].trim());
                        record.setProduct(parts[2].trim());
                        record.setPnlAmount(new BigDecimal(parts[3].trim()));
                        records.add(record);
                    } catch (Exception e) {
                        // Log or ignore invalid rows
                        System.err.println("Skipping invalid row: " + line + " Error: " + e.getMessage());
                    }
                }
            }
        }

        if (!records.isEmpty()) {
            pnlRecordRepository.saveAll(records);
        }

        return Collections.singletonMap("recordsInserted", records.size());
    }
}
