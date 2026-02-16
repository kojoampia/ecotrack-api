package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.ReportStatus;
import com.ecotrack.api.domain.enumeration.ReportType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A DTO for the {@link com.ecotrack.api.domain.ComplianceReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComplianceReportDTO implements Serializable {

    private Long id;

    @NotNull(message = "Tenant ID cannot be null")
    private String tenantId;

    private String emissionReportId;

    @NotNull(message = "Report type cannot be null")
    private ReportType reportType;

    @NotNull(message = "Reporting period cannot be null")
    private String reportingPeriod;

    @NotNull(message = "Status cannot be null")
    private ReportStatus status;

    private Instant generatedAt;

    private Instant submittedAt;

    private String submissionReference;

    private BigDecimal totalEmissions;

    private BigDecimal scope1Emissions;

    private BigDecimal scope2Emissions;

    private BigDecimal scope3Emissions;

    private Integer confidenceLevel;

    private String reviewNotes;

    private Instant createdDate;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmissionReportId() {
        return emissionReportId;
    }

    public void setEmissionReportId(String emissionReportId) {
        this.emissionReportId = emissionReportId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(String reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSubmissionReference() {
        return submissionReference;
    }

    public void setSubmissionReference(String submissionReference) {
        this.submissionReference = submissionReference;
    }

    public BigDecimal getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(BigDecimal totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public BigDecimal getScope1Emissions() {
        return scope1Emissions;
    }

    public void setScope1Emissions(BigDecimal scope1Emissions) {
        this.scope1Emissions = scope1Emissions;
    }

    public BigDecimal getScope2Emissions() {
        return scope2Emissions;
    }

    public void setScope2Emissions(BigDecimal scope2Emissions) {
        this.scope2Emissions = scope2Emissions;
    }

    public BigDecimal getScope3Emissions() {
        return scope3Emissions;
    }

    public void setScope3Emissions(BigDecimal scope3Emissions) {
        this.scope3Emissions = scope3Emissions;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplianceReportDTO)) {
            return false;
        }

        ComplianceReportDTO complianceReportDTO = (ComplianceReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(complianceReportDTO.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "ComplianceReportDTO{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            "'" +
            ", reportType='" +
            reportType +
            "'" +
            ", reportingPeriod='" +
            reportingPeriod +
            "'" +
            ", status='" +
            status +
            "'" +
            "}"
        );
    }
}
