package com.ecotrack.api.domain;

import com.ecotrack.api.domain.enumeration.ReportStatus;
import com.ecotrack.api.domain.enumeration.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ComplianceReport.
 */
@Entity
@Table(name = "compliance_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ComplianceReport extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "emission_report_id", length = 100)
    private String emissionReportId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;

    @NotNull
    @Column(name = "reporting_period", nullable = false, length = 50)
    private String reportingPeriod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ReportStatus status;

    @Column(name = "generated_at")
    private Instant generatedAt;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "submission_reference", length = 100)
    private String submissionReference;

    @Column(name = "total_emissions", precision = 21, scale = 2)
    private BigDecimal totalEmissions;

    @Column(name = "scope_1_emissions", precision = 21, scale = 2)
    private BigDecimal scope1Emissions;

    @Column(name = "scope_2_emissions", precision = 21, scale = 2)
    private BigDecimal scope2Emissions;

    @Column(name = "scope_3_emissions", precision = 21, scale = 2)
    private BigDecimal scope3Emissions;

    @Column(name = "confidence_level")
    private Integer confidenceLevel;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplianceReport)) {
            return false;
        }
        return id != null && id.equals(((ComplianceReport) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "ComplianceReport{" +
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
