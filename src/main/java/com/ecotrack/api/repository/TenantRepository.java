package com.ecotrack.api.repository;

import com.ecotrack.api.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Tenant} entity.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {}
