package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.EmissionReport;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link EmissionReport} and its DTO {@link EmissionReportDTO}.
 */
@Service
public class EmissionReportMapper {

    public EmissionReportDTO toDto(EmissionReport entity) {
        if (entity == null) {
            return null;
        }

        EmissionReportDTO dto = new EmissionReportDTO();

        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setSupplierId(entity.getSupplierId());
        dto.setMetadata(entity.getMetadata());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        return dto;
    }

    public EmissionReport toEntity(EmissionReportDTO dto) {
        if (dto == null) {
            return null;
        }

        EmissionReport entity = new EmissionReport();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setSupplierId(dto.getSupplierId());
        entity.setMetadata(dto.getMetadata());

        return entity;
    }

    public void partialUpdate(EmissionReport entity, EmissionReportDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getSupplierId() != null) {
            entity.setSupplierId(dto.getSupplierId());
        }
        if (dto.getMetadata() != null) {
            entity.setMetadata(dto.getMetadata());
        }
    }
}
