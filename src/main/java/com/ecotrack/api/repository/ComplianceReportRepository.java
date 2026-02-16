package com.ecotrack.api.repository;

import com.ecotrack.api.domain.ComplianceReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ComplianceReport entity.
 */
@Repository
public interface ComplianceReportRepository extends JpaRepository<ComplianceReport, Long>, JpaSpecificationExecutor<ComplianceReport> {}
