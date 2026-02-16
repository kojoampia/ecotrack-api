package com.ecotrack.api.repository;

import com.ecotrack.api.domain.Supplier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Supplier} entity.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByTenantId(String tenantId);

    List<Supplier> findByIndustry(String industry);

    List<Supplier> findByTier(Integer tier);

    List<Supplier> findByTenantIdAndIndustry(String tenantId, String industry);

    Optional<Supplier> findByIdAndTenantId(Long id, String tenantId);

    List<Supplier> findByCompanyNameContainingIgnoreCase(String companyName);
}
