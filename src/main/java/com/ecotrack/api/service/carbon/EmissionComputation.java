package com.ecotrack.api.service.carbon;

import java.math.BigDecimal;

/**
 * Computed emission output from a strategy execution.
 */
public record EmissionComputation(
    Long carbonGrams,
    BigDecimal emissionFactorUsed,
    BigDecimal efficiencyRatioApplied,
    String calculationMethod
) {}
