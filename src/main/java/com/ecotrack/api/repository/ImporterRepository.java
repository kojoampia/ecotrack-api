package com.ecotrack.api.repository;

import com.ecotrack.api.domain.Importer;
import com.ecotrack.api.domain.enumeration.Sector;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Importer} entity.
 */
@Repository
public interface ImporterRepository extends JpaRepository<Importer, Long> {
    List<Importer> findByTenantId(String tenantId);

    List<Importer> findByCountry(String country);

    List<Importer> findBySector(Sector sector);

    List<Importer> findByTenantIdAndCountry(String tenantId, String country);

    Optional<Importer> findByIdAndTenantId(Long id, String tenantId);

    List<Importer> findByCompanyNameContainingIgnoreCase(String companyName);

    Optional<Importer> findByEoriNumber(String eoriNumber);
}
