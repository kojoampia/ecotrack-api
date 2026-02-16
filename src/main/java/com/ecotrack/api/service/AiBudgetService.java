package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.AiBudgetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.AiBudget}.
 */
public interface AiBudgetService {
    AiBudgetDTO save(AiBudgetDTO aiBudgetDTO);

    AiBudgetDTO update(AiBudgetDTO aiBudgetDTO);

    Optional<AiBudgetDTO> partialUpdate(AiBudgetDTO aiBudgetDTO);

    Page<AiBudgetDTO> findAll(Pageable pageable);

    Page<AiBudgetDTO> findByTenantId(String tenantId, Pageable pageable);

    Optional<AiBudgetDTO> findOne(Long id);

    void delete(Long id);
}
