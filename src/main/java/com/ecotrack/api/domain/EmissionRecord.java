package com.ecotrack.api.domain;

import com.ecotrack.api.domain.converter.RecordMetadataConverter;
import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.utils.RecordMetadata;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmissionRecord.
 */
@Entity
@Table(name = "emission_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmissionRecord extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "supplier_id", length = 100)
    private String supplierId;

    @Column(name = "installation_id", length = 100)
    private String installationId;

    @Column(name = "product_emission_id", length = 100)
    private String productEmissionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false)
    private Scope scope;

    @NotNull
    @Column(name = "carbon_grams", nullable = false)
    private Long carbonGrams;

    @Column(name = "date_recorded")
    private LocalDate dateRecorded;

    @Column(name = "source", columnDefinition = "TEXT")
    private String source;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "confidence_score")
    private Integer confidenceScore;

    @Column(name = "calculation_method", columnDefinition = "TEXT")
    private String calculationMethod;

    @Column(name = "uncertainty_factor", precision = 21, scale = 4)
    private BigDecimal uncertaintyFactor;

    @Convert(converter = RecordMetadataConverter.class)
    @Column(name = "metadata", columnDefinition = "TEXT")
    private RecordMetadata metadata;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private EmissionReport report;

    // Constructors
    public EmissionRecord() {}

    public EmissionRecord(String tenantId, Scope scope, Long carbonGrams) {
        this.tenantId = tenantId;
        this.scope = scope;
        this.carbonGrams = carbonGrams;
    }

    // Getters and Setters
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getProductEmissionId() {
        return productEmissionId;
    }

    public void setProductEmissionId(String productEmissionId) {
        this.productEmissionId = productEmissionId;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Long getCarbonGrams() {
        return carbonGrams;
    }

    public void setCarbonGrams(Long carbonGrams) {
        this.carbonGrams = carbonGrams;
    }

    public LocalDate getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDate dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public BigDecimal getUncertaintyFactor() {
        return uncertaintyFactor;
    }

    public void setUncertaintyFactor(BigDecimal uncertaintyFactor) {
        this.uncertaintyFactor = uncertaintyFactor;
    }

    public RecordMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RecordMetadata metadata) {
        this.metadata = metadata;
    }

    public EmissionReport getReport() {
        return report;
    }

    public void setReport(EmissionReport report) {
        this.report = report;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionRecord)) {
            return false;
        }
        return id != null && id.equals(((EmissionRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "EmissionRecord{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", supplierId='" +
            supplierId +
            '\'' +
            ", installationId='" +
            installationId +
            '\'' +
            ", productEmissionId='" +
            productEmissionId +
            '\'' +
            ", scope=" +
            scope +
            ", carbonGrams=" +
            carbonGrams +
            ", dateRecorded=" +
            dateRecorded +
            ", verified=" +
            verified +
            ", confidenceScore=" +
            confidenceScore +
            '}'
        );
    }
}
