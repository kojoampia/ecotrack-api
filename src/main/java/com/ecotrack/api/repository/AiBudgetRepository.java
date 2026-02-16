package com.ecotrack.api.repository;

import com.ecotrack.api.domain.AiBudget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link AiBudget} entity.
 */
@Repository
public interface AiBudgetRepository extends JpaRepository<AiBudget, Long> {
    Page<AiBudget> findByTenantId(String tenantId, Pageable pageable);
}
