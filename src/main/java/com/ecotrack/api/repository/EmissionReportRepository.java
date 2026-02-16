package com.ecotrack.api.repository;

import com.ecotrack.api.domain.EmissionReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmissionReport entity.
 */
@Repository
public interface EmissionReportRepository extends JpaRepository<EmissionReport, Long>, JpaSpecificationExecutor<EmissionReport> {}
