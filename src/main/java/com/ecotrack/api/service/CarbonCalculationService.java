package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import com.ecotrack.api.service.dto.CarbonCalculationResultDTO;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.dto.EmissionReportDTO;

/**
 * Service for calculating carbon emissions with scope-aware strategies.
 */
public interface CarbonCalculationService {
    /**
     * Calculate emissions without persisting a record.
     */
    CarbonCalculationResultDTO calculate(CarbonCalculationRequestDTO request);

    /**
     * Calculate emissions and persist an emission record.
     */
    EmissionRecordDTO calculateAndSaveEmissionRecord(CarbonCalculationRequestDTO request);

    /**
     * Calculate emissions and map the result into an emission report DTO.
     */
    EmissionReportDTO calculateToEmissionReport(CarbonCalculationRequestDTO request);
}
