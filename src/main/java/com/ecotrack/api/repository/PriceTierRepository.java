package com.ecotrack.api.repository;

import com.ecotrack.api.domain.PriceTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link PriceTier} entity.
 */
@Repository
public interface PriceTierRepository extends JpaRepository<PriceTier, Long> {}
