package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.ComplianceReport;
import com.ecotrack.api.service.dto.ComplianceReportDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link ComplianceReport} and its DTO {@link ComplianceReportDTO}.
 */
@Service
public class ComplianceReportMapper {

    public ComplianceReportDTO toDto(ComplianceReport entity) {
        if (entity == null) {
            return null;
        }

        ComplianceReportDTO dto = new ComplianceReportDTO();

        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setEmissionReportId(entity.getEmissionReportId());
        dto.setReportType(entity.getReportType());
        dto.setReportingPeriod(entity.getReportingPeriod());
        dto.setStatus(entity.getStatus());
        dto.setGeneratedAt(entity.getGeneratedAt());
        dto.setSubmittedAt(entity.getSubmittedAt());
        dto.setSubmissionReference(entity.getSubmissionReference());
        dto.setTotalEmissions(entity.getTotalEmissions());
        dto.setScope1Emissions(entity.getScope1Emissions());
        dto.setScope2Emissions(entity.getScope2Emissions());
        dto.setScope3Emissions(entity.getScope3Emissions());
        dto.setConfidenceLevel(entity.getConfidenceLevel());
        dto.setReviewNotes(entity.getReviewNotes());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        return dto;
    }

    public ComplianceReport toEntity(ComplianceReportDTO dto) {
        if (dto == null) {
            return null;
        }

        ComplianceReport entity = new ComplianceReport();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setEmissionReportId(dto.getEmissionReportId());
        entity.setReportType(dto.getReportType());
        entity.setReportingPeriod(dto.getReportingPeriod());
        entity.setStatus(dto.getStatus());
        entity.setGeneratedAt(dto.getGeneratedAt());
        entity.setSubmittedAt(dto.getSubmittedAt());
        entity.setSubmissionReference(dto.getSubmissionReference());
        entity.setTotalEmissions(dto.getTotalEmissions());
        entity.setScope1Emissions(dto.getScope1Emissions());
        entity.setScope2Emissions(dto.getScope2Emissions());
        entity.setScope3Emissions(dto.getScope3Emissions());
        entity.setConfidenceLevel(dto.getConfidenceLevel());
        entity.setReviewNotes(dto.getReviewNotes());

        return entity;
    }

    public void partialUpdate(ComplianceReport entity, ComplianceReportDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getEmissionReportId() != null) {
            entity.setEmissionReportId(dto.getEmissionReportId());
        }
        if (dto.getReportType() != null) {
            entity.setReportType(dto.getReportType());
        }
        if (dto.getReportingPeriod() != null) {
            entity.setReportingPeriod(dto.getReportingPeriod());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getGeneratedAt() != null) {
            entity.setGeneratedAt(dto.getGeneratedAt());
        }
        if (dto.getSubmittedAt() != null) {
            entity.setSubmittedAt(dto.getSubmittedAt());
        }
        if (dto.getSubmissionReference() != null) {
            entity.setSubmissionReference(dto.getSubmissionReference());
        }
        if (dto.getTotalEmissions() != null) {
            entity.setTotalEmissions(dto.getTotalEmissions());
        }
        if (dto.getScope1Emissions() != null) {
            entity.setScope1Emissions(dto.getScope1Emissions());
        }
        if (dto.getScope2Emissions() != null) {
            entity.setScope2Emissions(dto.getScope2Emissions());
        }
        if (dto.getScope3Emissions() != null) {
            entity.setScope3Emissions(dto.getScope3Emissions());
        }
        if (dto.getConfidenceLevel() != null) {
            entity.setConfidenceLevel(dto.getConfidenceLevel());
        }
        if (dto.getReviewNotes() != null) {
            entity.setReviewNotes(dto.getReviewNotes());
        }
    }
}
