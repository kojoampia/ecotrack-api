package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.AuditTrailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.AuditTrail}.
 */
public interface AuditTrailService {
    /**
     * Save a auditTrail.
     *
     * @param auditTrailDTO the entity to save.
     * @return the persisted entity.
     */
    AuditTrailDTO save(AuditTrailDTO auditTrailDTO);

    /**
     * Updates a auditTrail.
     *
     * @param auditTrailDTO the entity to update.
     * @return the persisted entity.
     */
    AuditTrailDTO update(AuditTrailDTO auditTrailDTO);

    /**
     * Partially updates a auditTrail.
     *
     * @param auditTrailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuditTrailDTO> partialUpdate(AuditTrailDTO auditTrailDTO);

    /**
     * Get all the auditTrails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuditTrailDTO> findAll(Pageable pageable);

    /**
     * Get the "id" auditTrail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuditTrailDTO> findOne(Long id);

    /**
     * Delete the "id" auditTrail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
