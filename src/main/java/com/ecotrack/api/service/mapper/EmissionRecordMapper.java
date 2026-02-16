package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.EmissionRecord;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link EmissionRecord} and its DTO {@link EmissionRecordDTO}.
 */
@Service
public class EmissionRecordMapper {

    public EmissionRecordDTO toDto(EmissionRecord entity) {
        if (entity == null) {
            return null;
        }

        EmissionRecordDTO dto = new EmissionRecordDTO();

        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setSupplierId(entity.getSupplierId());
        dto.setInstallationId(entity.getInstallationId());
        dto.setProductEmissionId(entity.getProductEmissionId());
        dto.setScope(entity.getScope());
        dto.setCarbonGrams(entity.getCarbonGrams());
        dto.setDateRecorded(entity.getDateRecorded());
        dto.setSource(entity.getSource());
        dto.setNotes(entity.getNotes());
        dto.setVerified(entity.getVerified());
        dto.setConfidenceScore(entity.getConfidenceScore());
        dto.setCalculationMethod(entity.getCalculationMethod());
        dto.setUncertaintyFactor(entity.getUncertaintyFactor());
        dto.setMetadata(entity.getMetadata());

        return dto;
    }

    public EmissionRecord toEntity(EmissionRecordDTO dto) {
        if (dto == null) {
            return null;
        }

        EmissionRecord entity = new EmissionRecord();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setSupplierId(dto.getSupplierId());
        entity.setInstallationId(dto.getInstallationId());
        entity.setProductEmissionId(dto.getProductEmissionId());
        entity.setScope(dto.getScope());
        entity.setCarbonGrams(dto.getCarbonGrams());
        entity.setDateRecorded(dto.getDateRecorded());
        entity.setSource(dto.getSource());
        entity.setNotes(dto.getNotes());
        entity.setVerified(dto.getVerified());
        entity.setConfidenceScore(dto.getConfidenceScore());
        entity.setCalculationMethod(dto.getCalculationMethod());
        entity.setUncertaintyFactor(dto.getUncertaintyFactor());
        entity.setMetadata(dto.getMetadata());

        return entity;
    }
}
