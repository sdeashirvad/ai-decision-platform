package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.dto.AnomalyCalculationResponseDto;

public interface AnomalyCalculationService {
    AnomalyCalculationResponseDto calculateAnomalies(String desk);
}
