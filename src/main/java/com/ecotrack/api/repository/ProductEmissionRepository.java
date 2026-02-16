package com.ecotrack.api.repository;

import com.ecotrack.api.domain.ProductEmission;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductEmission entity.
 */
@Repository
public interface ProductEmissionRepository extends JpaRepository<ProductEmission, Long> {
    @Query("select p from ProductEmission p where p.tenantId = :tenantId")
    Page<ProductEmission> findAllByTenantId(String tenantId, Pageable pageable);

    @Query("select p from ProductEmission p where p.cnCode = :cnCode")
    Optional<ProductEmission> findByCnCode(String cnCode);

    @Query("select p from ProductEmission p where p.productId = :productId and p.tenantId = :tenantId")
    Page<ProductEmission> findByProductIdAndTenantId(String productId, String tenantId, Pageable pageable);
}
