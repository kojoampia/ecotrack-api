package com.ecotrack.api.repository;

import com.ecotrack.api.domain.Lead;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Lead} entity.
 */
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    /**
     * Find a lead by email address.
     *
     * @param email the email address
     * @return the lead if found
     */
    Optional<Lead> findByEmail(String email);

    /**
     * Check if a lead with the given email exists.
     *
     * @param email the email address
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
