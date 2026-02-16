package com.ecotrack.api.repository;

import com.ecotrack.api.domain.EmissionRecord;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmissionRecord entity.
 */
@Repository
public interface EmissionRecordRepository extends JpaRepository<EmissionRecord, Long> {
    /**
     * Find all emission records for a tenant.
     */
    Page<EmissionRecord> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find emission records by supplier.
     */
    Page<EmissionRecord> findBySupplierIdAndTenantId(String supplierId, String tenantId, Pageable pageable);

    /**
     * Find verified emission records for a tenant.
     */
    List<EmissionRecord> findByTenantIdAndVerified(String tenantId, Boolean verified);

    /**
     * Custom query to calculate total emissions by scope and tenant.
     */
    @Query("SELECT SUM(er.carbonGrams) FROM EmissionRecord er WHERE er.tenantId = :tenantId AND er.scope = :scope")
    Long calculateTotalEmissionsByScope(@Param("tenantId") String tenantId, @Param("scope") String scope);
}
