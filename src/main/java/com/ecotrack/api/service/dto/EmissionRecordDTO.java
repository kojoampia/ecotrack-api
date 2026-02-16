package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.utils.RecordMetadata;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.ecotrack.api.domain.EmissionRecord} entity.
 */
public class EmissionRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String tenantId;

    private String supplierId;

    private String installationId;

    private String productEmissionId;

    @NotNull
    private Scope scope;

    @NotNull
    private Long carbonGrams;

    private LocalDate dateRecorded;

    private String source;

    private String notes;

    private Boolean verified;

    private Integer confidenceScore;

    private String calculationMethod;

    private BigDecimal uncertaintyFactor;

    private RecordMetadata metadata;

    public EmissionRecordDTO() {}

    public EmissionRecordDTO(String tenantId, Scope scope, Long carbonGrams) {
        this.tenantId = tenantId;
        this.scope = scope;
        this.carbonGrams = carbonGrams;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionRecordDTO)) {
            return false;
        }
        return id != null && id.equals(((EmissionRecordDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "EmissionRecordDTO{" +
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
