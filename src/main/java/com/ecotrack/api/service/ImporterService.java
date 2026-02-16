package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.ImporterDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.Importer}.
 */
public interface ImporterService {
    /**
     * Save an importer.
     *
     * @param importerDTO the entity to save
     * @return the persisted entity
     */
    ImporterDTO save(ImporterDTO importerDTO);

    /**
     * Updates an importer.
     *
     * @param importerDTO the entity to update
     * @return the persisted entity
     */
    ImporterDTO update(ImporterDTO importerDTO);

    /**
     * Partially updates an importer.
     *
     * @param importerDTO the entity to update partially
     * @return the persisted entity
     */
    Optional<ImporterDTO> partialUpdate(ImporterDTO importerDTO);

    /**
     * Get all the importers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ImporterDTO> findAll(Pageable pageable);

    /**
     * Get all the importers by tenant.
     *
     * @param tenantId the tenant ID
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ImporterDTO> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Get all importers by country.
     *
     * @param country the country
     * @return the list of entities
     */
    List<ImporterDTO> findByCountry(String country);

    /**
     * Get the "id" importer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ImporterDTO> findOne(Long id);

    /**
     * Get an importer by EORI number.
     *
     * @param eoriNumber the EORI number
     * @return the entity
     */
    Optional<ImporterDTO> findByEoriNumber(String eoriNumber);

    /**
     * Delete the "id" importer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
