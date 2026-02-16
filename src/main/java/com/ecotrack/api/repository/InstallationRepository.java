package com.ecotrack.api.repository;

import com.ecotrack.api.domain.Installation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Installation entity.
 */
@Repository
public interface InstallationRepository extends JpaRepository<Installation, Long> {
    Page<Installation> findByTenantId(String tenantId, Pageable pageable);

    List<Installation> findByTenantId(String tenantId);

    Page<Installation> findBySupplierIdAndTenantId(Long supplierId, String tenantId, Pageable pageable);

    List<Installation> findBySupplierIdAndTenantId(Long supplierId, String tenantId);

    Optional<Installation> findByIdAndTenantId(Long id, String tenantId);
}
