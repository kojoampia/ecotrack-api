package com.ecotrack.api.service.carbon;

import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;

abstract class AbstractEmissionCalculationStrategy implements EmissionCalculationStrategy {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal ONE = BigDecimal.ONE;

    protected BigDecimal requireEmissionFactor(CarbonCalculationRequestDTO request) {
        if (request.getEmissionFactor() == null) {
            throw new IllegalArgumentException("Emission factor is required for scope " + request.getScope());
        }
        return request.getEmissionFactor();
    }

    protected EmissionComputation compute(CarbonCalculationRequestDTO request, BigDecimal emissionFactor, String calculationMethod) {
        BigDecimal efficiencyRatio = request.getEfficiencyRatio() == null ? ZERO : request.getEfficiencyRatio();
        BigDecimal emissions = request.getActivityData().multiply(emissionFactor).multiply(ONE.subtract(efficiencyRatio));
        Long carbonGrams = emissions.setScale(0, RoundingMode.HALF_UP).longValueExact();
        return new EmissionComputation(carbonGrams, emissionFactor, efficiencyRatio, calculationMethod);
    }
}
