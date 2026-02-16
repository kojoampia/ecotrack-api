package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.EmissionEvidenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.EmissionEvidence}.
 */
public interface EmissionEvidenceService {
    /**
     * Save an emission evidence.
     *
     * @param emissionEvidenceDTO the entity to save
     * @return the persisted entity
     */
    EmissionEvidenceDTO save(EmissionEvidenceDTO emissionEvidenceDTO);

    /**
     * Updates an emission evidence.
     *
     * @param emissionEvidenceDTO the entity to update
     * @return the persisted entity
     */
    EmissionEvidenceDTO update(EmissionEvidenceDTO emissionEvidenceDTO);

    /**
     * Partially updates an emission evidence.
     *
     * @param emissionEvidenceDTO the entity to update partially
     * @return the persisted entity
     */
    Optional<EmissionEvidenceDTO> partialUpdate(EmissionEvidenceDTO emissionEvidenceDTO);

    /**
     * Get all the emission evidence.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EmissionEvidenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emission evidence.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EmissionEvidenceDTO> findOne(Long id);

    /**
     * Delete the "id" emission evidence.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
