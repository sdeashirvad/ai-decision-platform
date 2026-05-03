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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnomalyCalculationServiceImpl implements AnomalyCalculationService {

    private static final BigDecimal THRESHOLD_MEDIUM = new BigDecimal("100000");
    private static final BigDecimal THRESHOLD_HIGH = new BigDecimal("200000");

    private final PnlRecordRepository pnlRecordRepository;
    private final AnomalyRecordRepository anomalyRecordRepository;

    private int anomaliesInserted = 0;
    private int anomaliesUpdated = 0;

    public AnomalyCalculationServiceImpl(PnlRecordRepository pnlRecordRepository, AnomalyRecordRepository anomalyRecordRepository) {
        this.pnlRecordRepository = pnlRecordRepository;
        this.anomalyRecordRepository = anomalyRecordRepository;
    }

    @Override
    @Transactional
    public AnomalyCalculationResponseDto calculateAnomalies(String desk) {
        anomaliesInserted = 0;
        anomaliesUpdated = 0;

        List<PnlRecord> records;
        if (desk != null && !desk.isEmpty()) {
            records = pnlRecordRepository.findAll().stream()
                    .filter(r -> r.getDesk().equals(desk))
                    .collect(Collectors.toList());
        } else {
            records = pnlRecordRepository.findAll();
        }

        Map<String, List<PnlRecord>> recordsByDesk = records.stream()
                .collect(Collectors.groupingBy(PnlRecord::getDesk));

        for (Map.Entry<String, List<PnlRecord>> entry : recordsByDesk.entrySet()) {
            List<PnlRecord> deskRecords = entry.getValue();
            if (deskRecords.isEmpty()) continue;

            BigDecimal sum = deskRecords.stream()
                    .map(PnlRecord::getPnlAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal count = new BigDecimal(deskRecords.size());
            BigDecimal mean = sum.divide(count, 2, RoundingMode.HALF_UP);

            deskRecords.forEach(record -> processPnlRecordForAnomalies(record, mean));
        }

        return new AnomalyCalculationResponseDto(anomaliesInserted, anomaliesUpdated);
    }

    private void processPnlRecordForAnomalies(PnlRecord record, BigDecimal mean) {
        BigDecimal deviation = record.getPnlAmount().subtract(mean).abs();

        if (deviation.compareTo(THRESHOLD_MEDIUM) > 0) {
            String severity = (deviation.compareTo(THRESHOLD_HIGH) > 0) ? "HIGH" : "MEDIUM";

            Optional<AnomalyRecord> existingAnomaly = anomalyRecordRepository.findByDateAndDeskAndProduct(
                record.getDate(), record.getDesk(), record.getProduct()
            );

            if (existingAnomaly.isPresent()) {
                AnomalyRecord anomalyToUpdate = existingAnomaly.get();
                anomalyToUpdate.setDeviation(deviation);
                anomalyToUpdate.setSeverity(severity);
                anomalyRecordRepository.save(anomalyToUpdate);
                anomaliesUpdated++;
            } else {
                AnomalyRecord newAnomaly = new AnomalyRecord();
                newAnomaly.setDate(record.getDate());
                newAnomaly.setDesk(record.getDesk());
                newAnomaly.setProduct(record.getProduct());
                newAnomaly.setDeviation(deviation);
                newAnomaly.setSeverity(severity);
                anomalyRecordRepository.save(newAnomaly);
                anomaliesInserted++;
            }
        }
    }
}
