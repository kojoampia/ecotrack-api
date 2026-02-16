package com.ecotrack.api.service;

import com.ecotrack.api.service.dto.ComplianceReportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ecotrack.api.domain.ComplianceReport}.
 */
public interface ComplianceReportService {
    ComplianceReportDTO save(ComplianceReportDTO complianceReportDTO);

    ComplianceReportDTO update(ComplianceReportDTO complianceReportDTO);

    Optional<ComplianceReportDTO> partialUpdate(ComplianceReportDTO complianceReportDTO);

    Page<ComplianceReportDTO> findAll(Pageable pageable);

    Optional<ComplianceReportDTO> findOne(Long id);

    void delete(Long id);
}
