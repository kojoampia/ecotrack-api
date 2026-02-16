package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.PriceTierDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.PriceTier}.
 */
public interface PriceTierService {
    PriceTierDTO save(PriceTierDTO priceTierDTO);

    PriceTierDTO update(PriceTierDTO priceTierDTO);

    Optional<PriceTierDTO> partialUpdate(PriceTierDTO priceTierDTO);

    Page<PriceTierDTO> findAll(Pageable pageable);

    Optional<PriceTierDTO> findOne(Long id);

    void delete(Long id);
}
