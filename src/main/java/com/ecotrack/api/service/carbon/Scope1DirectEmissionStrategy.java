package com.ecotrack.api.service.carbon;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import org.springframework.stereotype.Component;

/**
 * Strategy for direct emissions (Scope 1).
 */
@Component
public class Scope1DirectEmissionStrategy extends AbstractEmissionCalculationStrategy {

    private static final String METHOD = "SCOPE_1_DIRECT:E=A*EF*(1-ER)";

    @Override
    public Scope supportsScope() {
        return Scope.SCOPE_1;
    }

    @Override
    public EmissionComputation calculate(CarbonCalculationRequestDTO request) {
        return compute(request, requireEmissionFactor(request), METHOD);
    }
}
