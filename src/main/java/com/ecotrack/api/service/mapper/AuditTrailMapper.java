package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.AuditTrail;
import com.ecotrack.api.service.dto.AuditTrailDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link AuditTrail} and its DTO {@link AuditTrailDTO}.
 */
@Service
public class AuditTrailMapper {

    public AuditTrailDTO toDto(AuditTrail entity) {
        if (entity == null) {
            return null;
        }

        AuditTrailDTO dto = new AuditTrailDTO();

        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setAction(entity.getAction());
        dto.setEntityType(entity.getEntityType());
        dto.setEntityId(entity.getEntityId());
        dto.setOldValue(entity.getOldValue());
        dto.setNewValue(entity.getNewValue());
        dto.setChangedBy(entity.getChangedBy());
        dto.setChangedAt(entity.getChangedAt());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        dto.setReason(entity.getReason());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        return dto;
    }

    public AuditTrail toEntity(AuditTrailDTO dto) {
        if (dto == null) {
            return null;
        }

        AuditTrail entity = new AuditTrail();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setAction(dto.getAction());
        entity.setEntityType(dto.getEntityType());
        entity.setEntityId(dto.getEntityId());
        entity.setOldValue(dto.getOldValue());
        entity.setNewValue(dto.getNewValue());
        entity.setChangedBy(dto.getChangedBy());
        entity.setChangedAt(dto.getChangedAt());
        entity.setIpAddress(dto.getIpAddress());
        entity.setUserAgent(dto.getUserAgent());
        entity.setReason(dto.getReason());

        return entity;
    }

    public void partialUpdate(AuditTrail entity, AuditTrailDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getAction() != null) {
            entity.setAction(dto.getAction());
        }
        if (dto.getEntityType() != null) {
            entity.setEntityType(dto.getEntityType());
        }
        if (dto.getEntityId() != null) {
            entity.setEntityId(dto.getEntityId());
        }
        if (dto.getOldValue() != null) {
            entity.setOldValue(dto.getOldValue());
        }
        if (dto.getNewValue() != null) {
            entity.setNewValue(dto.getNewValue());
        }
        if (dto.getChangedBy() != null) {
            entity.setChangedBy(dto.getChangedBy());
        }
        if (dto.getChangedAt() != null) {
            entity.setChangedAt(dto.getChangedAt());
        }
        if (dto.getIpAddress() != null) {
            entity.setIpAddress(dto.getIpAddress());
        }
        if (dto.getUserAgent() != null) {
            entity.setUserAgent(dto.getUserAgent());
        }
        if (dto.getReason() != null) {
            entity.setReason(dto.getReason());
        }
    }
}
