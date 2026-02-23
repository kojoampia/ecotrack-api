package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.CarbonCalculationService;
import com.ecotrack.api.service.EmissionRecordService;
import com.ecotrack.api.service.EmissionReportService;
import com.ecotrack.api.service.carbon.EmissionCalculationStrategy;
import com.ecotrack.api.service.carbon.EmissionComputation;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import com.ecotrack.api.service.dto.CarbonCalculationResultDTO;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import com.ecotrack.api.service.mapper.CarbonCalculationMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Strategy-based carbon calculation engine for Scope 1, 2 and 3 records.
 */
@Service
@Transactional
public class CarbonCalculatorService implements CarbonCalculationService {

    private static final Logger log = LoggerFactory.getLogger(CarbonCalculatorService.class);

    private final Map<Scope, EmissionCalculationStrategy> strategiesByScope;
    private final CarbonCalculationMapper carbonCalculationMapper;
    private final EmissionRecordService emissionRecordService;
    private final EmissionReportService emissionReportService;

    public CarbonCalculatorService(
        List<EmissionCalculationStrategy> emissionCalculationStrategies,
        CarbonCalculationMapper carbonCalculationMapper,
        EmissionRecordService emissionRecordService,
        EmissionReportService emissionReportService
    ) {
        this.strategiesByScope = emissionCalculationStrategies
            .stream()
            .collect(Collectors.toUnmodifiableMap(EmissionCalculationStrategy::supportsScope, Function.identity()));
        this.carbonCalculationMapper = carbonCalculationMapper;
        this.emissionRecordService = emissionRecordService;
        this.emissionReportService = emissionReportService;
    }

    @Override
    @Transactional(readOnly = true)
    public CarbonCalculationResultDTO calculate(CarbonCalculationRequestDTO request) {
        log.debug("Request to calculate emissions: {}", request);
        EmissionComputation computation = compute(request);
        return carbonCalculationMapper.toResultDTO(request, computation);
    }

    @Override
    public EmissionRecordDTO calculateAndSaveEmissionRecord(CarbonCalculationRequestDTO request) {
        log.debug("Request to calculate and save emission record: {}", request);
        EmissionComputation computation = compute(request);
        EmissionRecordDTO emissionRecordDTO = carbonCalculationMapper.toComputedEmissionRecordDTO(request, computation);
        return emissionRecordService.save(emissionRecordDTO);
    }

    @Override
    public EmissionReportDTO calculateToEmissionReport(CarbonCalculationRequestDTO request) {
        log.debug("Request to calculate and map to emission report: {}", request);
        validateReportRequest(request);
        EmissionComputation computation = compute(request);
        EmissionReportDTO emissionReportDTO = carbonCalculationMapper.toComputedEmissionReportDTO(request, computation);
        return emissionReportService.save(emissionReportDTO);
    }

    private EmissionComputation compute(CarbonCalculationRequestDTO request) {
        validateRequest(request);
        EmissionCalculationStrategy strategy = strategiesByScope.get(request.getScope());
        if (strategy == null) {
            throw new IllegalArgumentException("No calculation strategy configured for scope " + request.getScope());
        }
        return strategy.calculate(request);
    }

    private void validateRequest(CarbonCalculationRequestDTO request) {
        Objects.requireNonNull(request, "Calculation request cannot be null");

        if (!StringUtils.hasText(request.getTenantId())) {
            throw new IllegalArgumentException("Tenant ID is required");
        }
        if (request.getScope() == null) {
            throw new IllegalArgumentException("Scope is required");
        }
        if (request.getActivityData() == null || request.getActivityData().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Activity data must be greater than zero");
        }
        if (request.getEmissionFactor() != null && request.getEmissionFactor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Emission factor must be greater than zero");
        }
        if (
            request.getEfficiencyRatio() != null &&
            (request.getEfficiencyRatio().compareTo(BigDecimal.ZERO) < 0 || request.getEfficiencyRatio().compareTo(BigDecimal.ONE) > 0)
        ) {
            throw new IllegalArgumentException("Efficiency ratio must be between 0 and 1");
        }
    }

    private void validateReportRequest(CarbonCalculationRequestDTO request) {
        if (!StringUtils.hasText(request.getSupplierId())) {
            throw new IllegalArgumentException("Supplier ID is required for emission report mapping");
        }
    }
}
