package com.ashirvad.platform.pnl.repository;

import com.ashirvad.platform.pnl.model.AnomalyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnomalyRecordRepository extends JpaRepository<AnomalyRecord, UUID>, JpaSpecificationExecutor<AnomalyRecord> {
    List<AnomalyRecord> findTop5ByOrderByDateDesc();
    Optional<AnomalyRecord> findByDateAndDeskAndProduct(LocalDate date, String desk, String product);
}
