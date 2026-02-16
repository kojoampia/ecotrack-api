package com.ecotrack.api.repository;

import com.ecotrack.api.domain.SupplierType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link SupplierType} entity.
 */
@Repository
public interface SupplierTypeRepository extends JpaRepository<SupplierType, Long> {
    List<SupplierType> findByTenantId(String tenantId);

    List<SupplierType> findByTenantIdAndCategory(String tenantId, String category);

    Optional<SupplierType> findByIdAndTenantId(Long id, String tenantId);
}
