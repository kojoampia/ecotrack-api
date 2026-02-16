package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.EmissionEvidence;
import com.ecotrack.api.service.dto.EmissionEvidenceDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link EmissionEvidence} and its DTO {@link EmissionEvidenceDTO}.
 */
@Service
public class EmissionEvidenceMapper {

    public EmissionEvidenceDTO toDto(EmissionEvidence entity) {
        if (entity == null) {
            return null;
        }

        EmissionEvidenceDTO dto = new EmissionEvidenceDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setSupplierId(entity.getSupplierId());
        dto.setEvidenceType(entity.getEvidenceType());
        dto.setFileName(entity.getFileName());
        dto.setFilePath(entity.getFilePath());
        dto.setFileSize(entity.getFileSize());
        dto.setMimeType(entity.getMimeType());
        dto.setChecksum(entity.getChecksum());
        dto.setUploadedAt(entity.getUploadedAt());
        dto.setVerified(entity.getVerified());
        dto.setVerificationNotes(entity.getVerificationNotes());
        dto.setRetentionPeriod(entity.getRetentionPeriod());
        dto.setLegalReference(entity.getLegalReference());

        return dto;
    }

    public EmissionEvidence toEntity(EmissionEvidenceDTO dto) {
        if (dto == null) {
            return null;
        }

        EmissionEvidence entity = new EmissionEvidence();
        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setSupplierId(dto.getSupplierId());
        entity.setEvidenceType(dto.getEvidenceType());
        entity.setFileName(dto.getFileName());
        entity.setFilePath(dto.getFilePath());
        entity.setFileSize(dto.getFileSize());
        entity.setMimeType(dto.getMimeType());
        entity.setChecksum(dto.getChecksum());
        entity.setUploadedAt(dto.getUploadedAt());
        entity.setVerified(dto.getVerified());
        entity.setVerificationNotes(dto.getVerificationNotes());
        entity.setRetentionPeriod(dto.getRetentionPeriod());
        entity.setLegalReference(dto.getLegalReference());

        return entity;
    }

    public void partialUpdate(EmissionEvidence entity, EmissionEvidenceDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getSupplierId() != null) {
            entity.setSupplierId(dto.getSupplierId());
        }
        if (dto.getEvidenceType() != null) {
            entity.setEvidenceType(dto.getEvidenceType());
        }
        if (dto.getFileName() != null) {
            entity.setFileName(dto.getFileName());
        }
        if (dto.getFilePath() != null) {
            entity.setFilePath(dto.getFilePath());
        }
        if (dto.getFileSize() != null) {
            entity.setFileSize(dto.getFileSize());
        }
        if (dto.getMimeType() != null) {
            entity.setMimeType(dto.getMimeType());
        }
        if (dto.getChecksum() != null) {
            entity.setChecksum(dto.getChecksum());
        }
        if (dto.getUploadedAt() != null) {
            entity.setUploadedAt(dto.getUploadedAt());
        }
        if (dto.getVerified() != null) {
            entity.setVerified(dto.getVerified());
        }
        if (dto.getVerificationNotes() != null) {
            entity.setVerificationNotes(dto.getVerificationNotes());
        }
        if (dto.getRetentionPeriod() != null) {
            entity.setRetentionPeriod(dto.getRetentionPeriod());
        }
        if (dto.getLegalReference() != null) {
            entity.setLegalReference(dto.getLegalReference());
        }
    }
}
