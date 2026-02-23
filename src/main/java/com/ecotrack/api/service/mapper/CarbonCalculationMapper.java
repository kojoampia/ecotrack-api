package com.ecotrack.api.service.mapper;

import com.ecotrack.api.service.carbon.EmissionComputation;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import com.ecotrack.api.service.dto.CarbonCalculationResultDTO;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import com.ecotrack.api.service.utils.RecordMetadata;
import java.time.Instant;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

/**
 * Mapper for carbon calculation input and output DTOs.
 */
@Mapper(componentModel = "spring")
public interface CarbonCalculationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carbonGrams", ignore = true)
    @Mapping(target = "calculationMethod", ignore = true)
    EmissionRecordDTO toEmissionRecordDTO(CarbonCalculationRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    EmissionReportDTO toEmissionReportDTO(CarbonCalculationRequestDTO request);

    default EmissionRecordDTO toComputedEmissionRecordDTO(CarbonCalculationRequestDTO request, EmissionComputation computation) {
        EmissionRecordDTO dto = toEmissionRecordDTO(request);
        dto.setCarbonGrams(computation.carbonGrams());
        dto.setCalculationMethod(computation.calculationMethod());

        if (dto.getDateRecorded() == null) {
            dto.setDateRecorded(LocalDate.now());
        }
        if (!StringUtils.hasText(dto.getSource())) {
            dto.setSource("carbon-calculator-service");
        }
        return dto;
    }

    default CarbonCalculationResultDTO toResultDTO(CarbonCalculationRequestDTO request, EmissionComputation computation) {
        CarbonCalculationResultDTO resultDTO = new CarbonCalculationResultDTO();
        resultDTO.setScope(request.getScope());
        resultDTO.setActivityData(request.getActivityData());
        resultDTO.setCarbonGrams(computation.carbonGrams());
        resultDTO.setEmissionFactorUsed(computation.emissionFactorUsed());
        resultDTO.setEfficiencyRatioApplied(computation.efficiencyRatioApplied());
        resultDTO.setCalculationMethod(computation.calculationMethod());
        return resultDTO;
    }

    default EmissionReportDTO toComputedEmissionReportDTO(CarbonCalculationRequestDTO request, EmissionComputation computation) {
        EmissionReportDTO dto = toEmissionReportDTO(request);
        RecordMetadata metadata = dto.getMetadata() == null ? new RecordMetadata(request.getTenantId()) : dto.getMetadata();
        metadata.setTenantId(request.getTenantId());
        metadata.setStatus("CALCULATED_" + request.getScope().name());
        metadata.setRole(computation.calculationMethod());
        metadata.setSubmittedBy("carbon-calculator-service");
        metadata.setTimestamp(Instant.now());
        metadata.setVerified(request.getVerified());
        dto.setMetadata(metadata);
        return dto;
    }
}
