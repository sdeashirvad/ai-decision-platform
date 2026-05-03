package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.model.PnlRecord;
import com.ashirvad.platform.pnl.repository.PnlRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
public class PnlIngestionServiceImpl implements PnlIngestionService {

    private final PnlRecordRepository pnlRecordRepository;

    public PnlIngestionServiceImpl(PnlRecordRepository pnlRecordRepository) {
        this.pnlRecordRepository = pnlRecordRepository;
    }

    @Override
    @Transactional
    public Map<String, Integer> ingestPnlData(MultipartFile file) throws IOException {
        int recordsInserted = 0;
        int recordsUpdated = 0;
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
                        LocalDate date = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                        String desk = parts[1].trim();
                        String product = parts[2].trim();
                        BigDecimal pnlAmount = new BigDecimal(parts[3].trim());

                        Optional<PnlRecord> existingRecord = pnlRecordRepository.findByDateAndDeskAndProduct(date, desk, product);

                        if (existingRecord.isPresent()) {
                            PnlRecord recordToUpdate = existingRecord.get();
                            recordToUpdate.setPnlAmount(pnlAmount);
                            pnlRecordRepository.save(recordToUpdate);
                            recordsUpdated++;
                        } else {
                            PnlRecord newRecord = new PnlRecord();
                            newRecord.setDate(date);
                            newRecord.setDesk(desk);
                            newRecord.setProduct(product);
                            newRecord.setPnlAmount(pnlAmount);
                            pnlRecordRepository.save(newRecord);
                            recordsInserted++;
                        }
                    } catch (Exception e) {
                        // Log or ignore invalid rows
                        System.err.println("Skipping invalid row: " + line + " Error: " + e.getMessage());
                    }
                }
            }
        }

        return Map.of("recordsInserted", recordsInserted, "recordsUpdated", recordsUpdated);
    }
}
