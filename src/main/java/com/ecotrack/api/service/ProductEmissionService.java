package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.ProductEmissionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.ProductEmission}.
 */
public interface ProductEmissionService {
    /**
     * Save a product emission.
     *
     * @param productEmissionDTO the entity to save.
     * @return the persisted entity.
     */
    ProductEmissionDTO save(ProductEmissionDTO productEmissionDTO);

    /**
     * Updates a product emission.
     *
     * @param productEmissionDTO the entity to update.
     * @return the persisted entity.
     */
    ProductEmissionDTO update(ProductEmissionDTO productEmissionDTO);

    /**
     * Partially updates a product emission.
     *
     * @param productEmissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductEmissionDTO> partialUpdate(ProductEmissionDTO productEmissionDTO);

    /**
     * Get all the product emissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductEmissionDTO> findAll(Pageable pageable);

    /**
     * Get all the product emissions for a tenant.
     *
     * @param tenantId the tenant ID.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductEmissionDTO> findAllByTenantId(String tenantId, Pageable pageable);

    /**
     * Get the "id" product emission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductEmissionDTO> findOne(Long id);

    /**
     * Delete the "id" product emission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
