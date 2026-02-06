package com.ashirvad.platform.etl.service;

import com.ashirvad.platform.etl.dto.EtlDiagnosisRequestDto;
import com.ashirvad.platform.etl.dto.EtlDiagnosisResponseDto;

public interface EtlDiagnosisService {
    EtlDiagnosisResponseDto diagnose(EtlDiagnosisRequestDto request);
}
