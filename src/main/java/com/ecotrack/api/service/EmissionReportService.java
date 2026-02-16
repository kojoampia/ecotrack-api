package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.EmissionReportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.EmissionReport}.
 */
public interface EmissionReportService {
    /**
     * Save a emissionReport.
     *
     * @param emissionReportDTO the entity to save.
     * @return the persisted entity.
     */
    EmissionReportDTO save(EmissionReportDTO emissionReportDTO);

    /**
     * Updates a emissionReport.
     *
     * @param emissionReportDTO the entity to update.
     * @return the persisted entity.
     */
    EmissionReportDTO update(EmissionReportDTO emissionReportDTO);

    /**
     * Partially updates a emissionReport.
     *
     * @param emissionReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmissionReportDTO> partialUpdate(EmissionReportDTO emissionReportDTO);

    /**
     * Get all the emissionReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmissionReportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emissionReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmissionReportDTO> findOne(Long id);

    /**
     * Delete the "id" emissionReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
