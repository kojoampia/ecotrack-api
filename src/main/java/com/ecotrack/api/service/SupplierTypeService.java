package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.SupplierTypeDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.SupplierType}.
 */
public interface SupplierTypeService {
    /**
     * Save a supplierType.
     *
     * @param supplierTypeDTO the entity to save
     * @return the persisted entity
     */
    SupplierTypeDTO save(SupplierTypeDTO supplierTypeDTO);

    /**
     * Updates a supplierType.
     *
     * @param supplierTypeDTO the entity to update
     * @return the persisted entity
     */
    SupplierTypeDTO update(SupplierTypeDTO supplierTypeDTO);

    /**
     * Partially updates a supplierType.
     *
     * @param supplierTypeDTO the entity to update partially
     * @return the persisted entity
     */
    Optional<SupplierTypeDTO> partialUpdate(SupplierTypeDTO supplierTypeDTO);

    /**
     * Get all the supplierTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the supplierTypes by tenant.
     *
     * @param tenantId the tenant ID
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierTypeDTO> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Get the "id" supplierType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SupplierTypeDTO> findOne(Long id);

    /**
     * Delete the "id" supplierType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
