package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.dto.AnomalyRecordDto;
import com.ashirvad.platform.pnl.dto.PnlRecordDto;
import com.ashirvad.platform.pnl.model.AnomalyRecord;
import com.ashirvad.platform.pnl.model.PnlRecord;
import com.ashirvad.platform.pnl.repository.AnomalyRecordRepository;
import com.ashirvad.platform.pnl.repository.PnlRecordRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

@Service
public class PnlServiceImpl implements PnlService {

    private final PnlRecordRepository pnlRecordRepository;
    private final AnomalyRecordRepository anomalyRecordRepository;

    public PnlServiceImpl(PnlRecordRepository pnlRecordRepository, AnomalyRecordRepository anomalyRecordRepository) {
        this.pnlRecordRepository = pnlRecordRepository;
        this.anomalyRecordRepository = anomalyRecordRepository;
    }

    @Override
    public List<PnlRecordDto> getPnlRecords(String desk, LocalDate fromDate, LocalDate toDate) {
        Specification<PnlRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (desk != null && !desk.isEmpty()) {
                predicates.add(cb.equal(root.get("desk"), desk));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return pnlRecordRepository.findAll(spec).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnomalyRecordDto> getAnomalies(String desk, LocalDate fromDate, LocalDate toDate) {
        Specification<AnomalyRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (desk != null && !desk.isEmpty()) {
                predicates.add(cb.equal(root.get("desk"), desk));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return anomalyRecordRepository.findAll(spec).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePnlRecords(UUID id, LocalDate fromDate, LocalDate toDate, String desk) {
        if (id != null) {
            pnlRecordRepository.deleteById(id);
        } else {
            Specification<PnlRecord> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (desk != null && !desk.isEmpty()) {
                    predicates.add(cb.equal(root.get("desk"), desk));
                }
                if (fromDate != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
                }
                if (toDate != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            pnlRecordRepository.delete(spec);
        }
    }

    @Override
    @Transactional
    public void deleteAnomalies(UUID id, LocalDate fromDate, LocalDate toDate, String desk) {
        if (id != null) {
            anomalyRecordRepository.deleteById(id);
        } else {
            Specification<AnomalyRecord> spec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (desk != null && !desk.isEmpty()) {
                    predicates.add(cb.equal(root.get("desk"), desk));
                }
                if (fromDate != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("date"), fromDate));
                }
                if (toDate != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("date"), toDate));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            anomalyRecordRepository.delete(spec);
        }
    }

    @Override
    @Transactional
    public PnlRecordDto createPnlRecord(PnlRecordDto dto) {
        PnlRecord entity = new PnlRecord();
        entity.setDate(dto.getDate());
        entity.setDesk(dto.getDesk());
        entity.setProduct(dto.getProduct());
        entity.setPnlAmount(dto.getPnlAmount());

        PnlRecord saved = pnlRecordRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public AnomalyRecordDto createAnomalyRecord(AnomalyRecordDto dto) {
        AnomalyRecord entity = new AnomalyRecord();
        entity.setDate(dto.getDate());
        entity.setDesk(dto.getDesk());
        entity.setProduct(dto.getProduct());
        entity.setDeviation(dto.getDeviation());
        entity.setSeverity(dto.getSeverity());

        AnomalyRecord saved = anomalyRecordRepository.save(entity);
        return mapToDto(saved);
    }

    private PnlRecordDto mapToDto(PnlRecord entity) {
        return new PnlRecordDto(
                entity.getId(),
                entity.getDate(),
                entity.getDesk(),
                entity.getProduct(),
                entity.getPnlAmount()
        );
    }

    private AnomalyRecordDto mapToDto(AnomalyRecord entity) {
        return new AnomalyRecordDto(
                entity.getId(),
                entity.getDate(),
                entity.getDesk(),
                entity.getProduct(),
                entity.getDeviation(),
                entity.getSeverity()
        );
    }
}
