package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.utils.RecordMetadata;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Input payload for carbon calculations.
 */
public class CarbonCalculationRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String tenantId;

    private String supplierId;

    private String installationId;

    private String productEmissionId;

    @NotNull
    private Scope scope;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal activityData;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal emissionFactor;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private BigDecimal efficiencyRatio;

    private LocalDate dateRecorded;

    private String source;

    private String notes;

    private Boolean verified;

    private Integer confidenceScore;

    @DecimalMin("0.0")
    private BigDecimal uncertaintyFactor;

    private RecordMetadata metadata;

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

    public BigDecimal getActivityData() {
        return activityData;
    }

    public void setActivityData(BigDecimal activityData) {
        this.activityData = activityData;
    }

    public BigDecimal getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(BigDecimal emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public BigDecimal getEfficiencyRatio() {
        return efficiencyRatio;
    }

    public void setEfficiencyRatio(BigDecimal efficiencyRatio) {
        this.efficiencyRatio = efficiencyRatio;
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
}
