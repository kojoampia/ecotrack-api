package com.ecotrack.api.service.carbon;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import org.springframework.stereotype.Component;

/**
 * Strategy for supply-chain goods emissions (Scope 3).
 */
@Component
public class Scope3SupplyChainEmissionStrategy extends AbstractEmissionCalculationStrategy {

    private static final String METHOD = "SCOPE_3_SUPPLY_CHAIN:E=A*EF*(1-ER)";

    @Override
    public Scope supportsScope() {
        return Scope.SCOPE_3;
    }

    @Override
    public EmissionComputation calculate(CarbonCalculationRequestDTO request) {
        return compute(request, requireEmissionFactor(request), METHOD);
    }
}
