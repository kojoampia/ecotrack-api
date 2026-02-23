package com.ecotrack.api.service.carbon;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;

/**
 * Scope-specific strategy contract for carbon calculations.
 */
public interface EmissionCalculationStrategy {
    Scope supportsScope();

    EmissionComputation calculate(CarbonCalculationRequestDTO request);
}
