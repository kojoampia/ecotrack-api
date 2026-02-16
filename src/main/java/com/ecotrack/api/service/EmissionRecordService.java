package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.EmissionRecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.EmissionRecord}.
 */
public interface EmissionRecordService {
    /**
     * Save an emission record.
     *
     * @param emissionRecordDTO the entity to save
     * @return the persisted entity
     */
    EmissionRecordDTO save(EmissionRecordDTO emissionRecordDTO);

    /**
     * Updates an emission record.
     *
     * @param emissionRecordDTO the entity to update
     * @return the persisted entity
     */
    EmissionRecordDTO update(EmissionRecordDTO emissionRecordDTO);

    /**
     * Partially updates an emission record.
     *
     * @param emissionRecordDTO the entity to update partially
     * @return the persisted entity
     */
    Optional<EmissionRecordDTO> partialUpdate(EmissionRecordDTO emissionRecordDTO);

    /**
     * Get all the emission records.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EmissionRecordDTO> findAll(Pageable pageable);

    /**
     * Get all emission records for a tenant.
     *
     * @param tenantId the tenant ID
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EmissionRecordDTO> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Get the "id" emission record.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EmissionRecordDTO> findOne(Long id);

    /**
     * Delete the "id" emission record.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
