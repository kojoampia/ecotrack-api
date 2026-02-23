package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.Scope;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Result payload for a carbon calculation.
 */
public class CarbonCalculationResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Scope scope;

    private Long carbonGrams;

    private BigDecimal activityData;

    private BigDecimal emissionFactorUsed;

    private BigDecimal efficiencyRatioApplied;

    private String calculationMethod;

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

    public BigDecimal getActivityData() {
        return activityData;
    }

    public void setActivityData(BigDecimal activityData) {
        this.activityData = activityData;
    }

    public BigDecimal getEmissionFactorUsed() {
        return emissionFactorUsed;
    }

    public void setEmissionFactorUsed(BigDecimal emissionFactorUsed) {
        this.emissionFactorUsed = emissionFactorUsed;
    }

    public BigDecimal getEfficiencyRatioApplied() {
        return efficiencyRatioApplied;
    }

    public void setEfficiencyRatioApplied(BigDecimal efficiencyRatioApplied) {
        this.efficiencyRatioApplied = efficiencyRatioApplied;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }
}
