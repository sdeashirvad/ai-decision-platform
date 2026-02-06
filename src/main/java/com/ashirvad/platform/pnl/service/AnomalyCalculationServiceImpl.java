package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.dto.AnomalyCalculationResponseDto;
import com.ashirvad.platform.pnl.model.AnomalyRecord;
import com.ashirvad.platform.pnl.model.PnlRecord;
import com.ashirvad.platform.pnl.repository.AnomalyRecordRepository;
import com.ashirvad.platform.pnl.repository.PnlRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnomalyCalculationServiceImpl implements AnomalyCalculationService {

    private static final BigDecimal THRESHOLD_MEDIUM = new BigDecimal("100000");
    private static final BigDecimal THRESHOLD_HIGH = new BigDecimal("200000");

    private final PnlRecordRepository pnlRecordRepository;
    private final AnomalyRecordRepository anomalyRecordRepository;

    public AnomalyCalculationServiceImpl(PnlRecordRepository pnlRecordRepository, AnomalyRecordRepository anomalyRecordRepository) {
        this.pnlRecordRepository = pnlRecordRepository;
        this.anomalyRecordRepository = anomalyRecordRepository;
    }

    @Override
    @Transactional
    public AnomalyCalculationResponseDto calculateAnomalies(String desk) {
        List<PnlRecord> records;
        if (desk != null && !desk.isEmpty()) {
            // Assuming we can filter by desk using a custom query or specification.
            // Since I don't have a direct method for findByDesk in the repo interface yet, 
            // I'll fetch all and filter in memory or use the specification if available.
            // Ideally, I should add findByDesk to the repo, but for now, I'll use findAll and filter stream 
            // to avoid modifying the repo interface if not strictly necessary, 
            // but for performance, a repo method is better.
            // However, the previous steps added JpaSpecificationExecutor, so I can use that.
            // But to keep it simple and robust without constructing specs here:
            records = pnlRecordRepository.findAll().stream()
                    .filter(r -> r.getDesk().equals(desk))
                    .collect(Collectors.toList());
        } else {
            records = pnlRecordRepository.findAll();
        }

        Map<String, List<PnlRecord>> recordsByDesk = records.stream()
                .collect(Collectors.groupingBy(PnlRecord::getDesk));

        List<AnomalyRecord> anomaliesToSave = new ArrayList<>();

        for (Map.Entry<String, List<PnlRecord>> entry : recordsByDesk.entrySet()) {
            String currentDesk = entry.getKey();
            List<PnlRecord> deskRecords = entry.getValue();

            if (deskRecords.isEmpty()) continue;

            BigDecimal sum = deskRecords.stream()
                    .map(PnlRecord::getPnlAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal count = new BigDecimal(deskRecords.size());
            BigDecimal mean = sum.divide(count, 2, RoundingMode.HALF_UP);

            for (PnlRecord record : deskRecords) {
                BigDecimal deviation = record.getPnlAmount().subtract(mean).abs();

                if (deviation.compareTo(THRESHOLD_MEDIUM) > 0) {
                    AnomalyRecord anomaly = new AnomalyRecord();
                    anomaly.setDate(record.getDate());
                    anomaly.setDesk(currentDesk);
                    anomaly.setDeviation(deviation);
                    
                    if (deviation.compareTo(THRESHOLD_HIGH) > 0) {
                        anomaly.setSeverity("HIGH");
                    } else {
                        anomaly.setSeverity("MEDIUM");
                    }
                    
                    anomaliesToSave.add(anomaly);
                }
            }
        }

        if (!anomaliesToSave.isEmpty()) {
            anomalyRecordRepository.saveAll(anomaliesToSave);
        }

        return new AnomalyCalculationResponseDto(anomaliesToSave.size());
    }
}
