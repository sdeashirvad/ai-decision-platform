package com.ashirvad.platform.pnl.service;

import com.ashirvad.platform.pnl.dto.ExplainAnomalyRequestDto;
import com.ashirvad.platform.pnl.dto.ExplainAnomalyResponseDto;

public interface PnlExplanationService {
    ExplainAnomalyResponseDto explainAnomaly(ExplainAnomalyRequestDto request);
}
