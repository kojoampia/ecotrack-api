package com.ecotrack.api.repository;

import com.ecotrack.api.domain.EmissionEvidence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EmissionEvidence} entity.
 */
@Repository
public interface EmissionEvidenceRepository extends JpaRepository<EmissionEvidence, Long> {
    List<EmissionEvidence> findByTenantId(String tenantId);

    List<EmissionEvidence> findBySupplierIdAndTenantId(String supplierId, String tenantId);
}
