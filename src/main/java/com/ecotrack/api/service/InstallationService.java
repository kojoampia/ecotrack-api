package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.InstallationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.Installation}.
 */
public interface InstallationService {
    /**
     * Save a installation.
     *
     * @param installationDTO the entity to save.
     * @return the persisted entity.
     */
    InstallationDTO save(InstallationDTO installationDTO);

    /**
     * Update a installation.
     *
     * @param installationDTO the entity to update.
     * @return the persisted entity.
     */
    InstallationDTO update(InstallationDTO installationDTO);

    /**
     * Partially updates a installation.
     *
     * @param installationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InstallationDTO> partialUpdate(InstallationDTO installationDTO);

    /**
     * Get all the installations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstallationDTO> findAll(Pageable pageable);

    /**
     * Get all the installations for tenant.
     *
     * @param tenantId the tenant ID.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstallationDTO> findAllByTenant(String tenantId, Pageable pageable);

    /**
     * Get the "id" installation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InstallationDTO> findOne(Long id);

    /**
     * Get the installation by id and tenant.
     *
     * @param id the id of the entity.
     * @param tenantId the tenant ID.
     * @return the entity.
     */
    Optional<InstallationDTO> findOneByTenant(Long id, String tenantId);

    /**
     * Delete the "id" installation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the installation by id and tenant.
     *
     * @param id the id of the entity.
     * @param tenantId the tenant ID.
     */
    void deleteByTenant(Long id, String tenantId);
}
