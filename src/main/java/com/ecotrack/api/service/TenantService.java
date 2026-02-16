package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.TenantDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.Tenant}.
 */
public interface TenantService {
    TenantDTO save(TenantDTO tenantDTO);

    TenantDTO update(TenantDTO tenantDTO);

    Optional<TenantDTO> findOne(String id);

    void delete(String id);
}
