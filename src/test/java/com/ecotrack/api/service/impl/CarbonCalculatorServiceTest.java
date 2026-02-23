package com.ecotrack.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.EmissionRecordService;
import com.ecotrack.api.service.EmissionReportService;
import com.ecotrack.api.service.carbon.EmissionCalculationStrategy;
import com.ecotrack.api.service.carbon.Scope1DirectEmissionStrategy;
import com.ecotrack.api.service.carbon.Scope2IndirectEmissionStrategy;
import com.ecotrack.api.service.carbon.Scope3SupplyChainEmissionStrategy;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import com.ecotrack.api.service.dto.CarbonCalculationResultDTO;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import com.ecotrack.api.service.mapper.CarbonCalculationMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CarbonCalculatorServiceTest {

    @Mock
    private EmissionRecordService emissionRecordService;

    @Mock
    private EmissionReportService emissionReportService;

    private CarbonCalculatorService carbonCalculatorService;

    @BeforeEach
    void setUp() {
        CarbonCalculationMapper mapper = Mappers.getMapper(CarbonCalculationMapper.class);
        List<EmissionCalculationStrategy> strategies = List.of(
            new Scope1DirectEmissionStrategy(),
            new Scope2IndirectEmissionStrategy(),
            new Scope3SupplyChainEmissionStrategy()
        );
        carbonCalculatorService = new CarbonCalculatorService(strategies, mapper, emissionRecordService, emissionReportService);
    }

    @Test
    void shouldCalculateScope1UsingFormula() {
        CarbonCalculationRequestDTO request = baseRequest(
            Scope.SCOPE_1,
            new BigDecimal("100"),
            new BigDecimal("2.5"),
            new BigDecimal("0.1")
        );

        CarbonCalculationResultDTO result = carbonCalculatorService.calculate(request);

        assertThat(result.getCarbonGrams()).isEqualTo(225L);
        assertThat(result.getEmissionFactorUsed()).isEqualByComparingTo("2.5");
        assertThat(result.getEfficiencyRatioApplied()).isEqualByComparingTo("0.1");
        assertThat(result.getCalculationMethod()).contains("SCOPE_1_DIRECT");
    }

    @Test
    void shouldUseDefaultGridFactorForScope2WhenFactorMissing() {
        CarbonCalculationRequestDTO request = baseRequest(Scope.SCOPE_2, new BigDecimal("10"), null, null);

        CarbonCalculationResultDTO result = carbonCalculatorService.calculate(request);

        assertThat(result.getCarbonGrams()).isEqualTo(1250L);
        assertThat(result.getEmissionFactorUsed()).isEqualByComparingTo("125");
        assertThat(result.getEfficiencyRatioApplied()).isEqualByComparingTo("0");
        assertThat(result.getCalculationMethod()).contains("2026_EU_GRID");
    }

    @Test
    void shouldRejectMissingEmissionFactorForScope3() {
        CarbonCalculationRequestDTO request = baseRequest(Scope.SCOPE_3, new BigDecimal("10"), null, BigDecimal.ZERO);

        assertThatThrownBy(() -> carbonCalculatorService.calculate(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Emission factor is required");
    }

    @Test
    void shouldCalculateAndPersistEmissionRecord() {
        CarbonCalculationRequestDTO request = baseRequest(
            Scope.SCOPE_1,
            new BigDecimal("10"),
            new BigDecimal("20"),
            new BigDecimal("0.25")
        );

        when(emissionRecordService.save(any(EmissionRecordDTO.class))).thenAnswer(invocation -> {
            EmissionRecordDTO dto = invocation.getArgument(0);
            dto.setId(99L);
            return dto;
        });

        EmissionRecordDTO result = carbonCalculatorService.calculateAndSaveEmissionRecord(request);

        ArgumentCaptor<EmissionRecordDTO> captor = ArgumentCaptor.forClass(EmissionRecordDTO.class);
        verify(emissionRecordService).save(captor.capture());
        EmissionRecordDTO saved = captor.getValue();
        assertThat(saved.getCarbonGrams()).isEqualTo(150L);
        assertThat(saved.getCalculationMethod()).contains("SCOPE_1_DIRECT");
        assertThat(saved.getSource()).isEqualTo("carbon-calculator-service");
        assertThat(saved.getDateRecorded()).isNotNull();
        assertThat(result.getId()).isEqualTo(99L);
    }

    @Test
    void shouldRejectEfficiencyRatioAboveOne() {
        CarbonCalculationRequestDTO request = baseRequest(
            Scope.SCOPE_2,
            new BigDecimal("10"),
            new BigDecimal("100"),
            new BigDecimal("1.1")
        );

        assertThatThrownBy(() -> carbonCalculatorService.calculate(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Efficiency ratio must be between 0 and 1");
    }

    @Test
    void shouldMapAndPersistEmissionReport() {
        CarbonCalculationRequestDTO request = baseRequest(Scope.SCOPE_2, new BigDecimal("5"), new BigDecimal("100"), BigDecimal.ZERO);
        request.setSupplierId("supplier-42");

        when(emissionReportService.save(any(EmissionReportDTO.class))).thenAnswer(invocation -> {
            EmissionReportDTO dto = invocation.getArgument(0);
            dto.setId(7L);
            return dto;
        });

        EmissionReportDTO result = carbonCalculatorService.calculateToEmissionReport(request);

        ArgumentCaptor<EmissionReportDTO> captor = ArgumentCaptor.forClass(EmissionReportDTO.class);
        verify(emissionReportService).save(captor.capture());
        EmissionReportDTO mapped = captor.getValue();
        assertThat(mapped.getTenantId()).isEqualTo("tenant-a");
        assertThat(mapped.getSupplierId()).isEqualTo("supplier-42");
        assertThat(mapped.getMetadata()).isNotNull();
        assertThat(mapped.getMetadata().getStatus()).isEqualTo("CALCULATED_SCOPE_2");
        assertThat(result.getId()).isEqualTo(7L);
    }

    @Test
    void shouldRejectReportMappingWhenSupplierMissing() {
        CarbonCalculationRequestDTO request = baseRequest(Scope.SCOPE_1, new BigDecimal("10"), new BigDecimal("2"), BigDecimal.ZERO);

        assertThatThrownBy(() -> carbonCalculatorService.calculateToEmissionReport(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Supplier ID is required");
    }

    private CarbonCalculationRequestDTO baseRequest(
        Scope scope,
        BigDecimal activityData,
        BigDecimal emissionFactor,
        BigDecimal efficiencyRatio
    ) {
        CarbonCalculationRequestDTO request = new CarbonCalculationRequestDTO();
        request.setTenantId("tenant-a");
        request.setScope(scope);
        request.setActivityData(activityData);
        request.setEmissionFactor(emissionFactor);
        request.setEfficiencyRatio(efficiencyRatio);
        return request;
    }
}
