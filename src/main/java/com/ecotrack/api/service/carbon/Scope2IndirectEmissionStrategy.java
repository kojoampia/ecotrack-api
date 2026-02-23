package com.ecotrack.api.service.carbon;

import com.ecotrack.api.domain.enumeration.Scope;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * Strategy for indirect emissions (Scope 2).
 */
@Component
public class Scope2IndirectEmissionStrategy extends AbstractEmissionCalculationStrategy {

    private static final BigDecimal DEFAULT_2026_EU_GRID_FACTOR = new BigDecimal("125");
    private static final String METHOD_WITH_DEFAULT = "SCOPE_2_INDIRECT_2026_EU_GRID:E=A*EF*(1-ER)";
    private static final String METHOD_WITH_INPUT = "SCOPE_2_INDIRECT_CUSTOM_FACTOR:E=A*EF*(1-ER)";

    @Override
    public Scope supportsScope() {
        return Scope.SCOPE_2;
    }

    @Override
    public EmissionComputation calculate(CarbonCalculationRequestDTO request) {
        BigDecimal emissionFactor = request.getEmissionFactor() == null ? DEFAULT_2026_EU_GRID_FACTOR : request.getEmissionFactor();
        String method = request.getEmissionFactor() == null ? METHOD_WITH_DEFAULT : METHOD_WITH_INPUT;
        return compute(request, emissionFactor, method);
    }
}
