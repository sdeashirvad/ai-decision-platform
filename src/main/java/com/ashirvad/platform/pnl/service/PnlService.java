package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.dto.AnomalyRecordDto;
import com.ashirvad.platform.pnl.dto.PnlRecordDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PnlService {
    List<PnlRecordDto> getPnlRecords(String desk, LocalDate fromDate, LocalDate toDate);
    List<AnomalyRecordDto> getAnomalies(String desk, LocalDate fromDate, LocalDate toDate);
    void deletePnlRecords(UUID id, LocalDate fromDate, LocalDate toDate, String desk);
    void deleteAnomalies(UUID id, LocalDate fromDate, LocalDate toDate, String desk);
    PnlRecordDto createPnlRecord(PnlRecordDto pnlRecordDto);
    AnomalyRecordDto createAnomalyRecord(AnomalyRecordDto anomalyRecordDto);
}
