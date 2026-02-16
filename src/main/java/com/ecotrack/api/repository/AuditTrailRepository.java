package com.ecotrack.api.repository;

import com.ecotrack.api.domain.AuditTrail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AuditTrail entity.
 */
@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, Long>, JpaSpecificationExecutor<AuditTrail> {}
