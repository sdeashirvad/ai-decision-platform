package com.ashirvad.platform.pnl.repository;

import com.ashirvad.platform.pnl.model.PnlRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PnlRecordRepository extends JpaRepository<PnlRecord, UUID>, JpaSpecificationExecutor<PnlRecord> {
    List<PnlRecord> findByDeskAndDate(String desk, LocalDate date);

    Optional<PnlRecord> findByDateAndDeskAndProduct(LocalDate date, String desk, String product);

    @Query("SELECT AVG(p.pnlAmount) FROM PnlRecord p WHERE p.desk = :desk AND p.date BETWEEN :startDate AND :endDate")
    BigDecimal findAveragePnlForDesk(@Param("desk") String desk, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
