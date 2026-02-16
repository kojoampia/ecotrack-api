package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.SupplierDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save
     * @return the persisted entity
     */
    SupplierDTO save(SupplierDTO supplierDTO);

    /**
     * Updates a supplier.
     *
     * @param supplierDTO the entity to update
     * @return the persisted entity
     */
    SupplierDTO update(SupplierDTO supplierDTO);

    /**
     * Partially updates a supplier.
     *
     * @param supplierDTO the entity to update partially
     * @return the persisted entity
     */
    Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO);

    /**
     * Get all the suppliers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierDTO> findAll(Pageable pageable);

    /**
     * Get all the suppliers by tenant.
     *
     * @param tenantId the tenant ID
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SupplierDTO> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Get all suppliers by industry.
     *
     * @param industry the industry
     * @return the list of entities
     */
    List<SupplierDTO> findByIndustry(String industry);

    /**
     * Get all suppliers by tier.
     *
     * @param tier the tier
     * @return the list of entities
     */
    List<SupplierDTO> findByTier(Integer tier);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SupplierDTO> findOne(Long id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
