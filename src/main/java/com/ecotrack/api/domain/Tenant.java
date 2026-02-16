package com.ecotrack.api.domain;

import com.ecotrack.api.domain.enumeration.Sector;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tenant.
 */
@Entity
@Table(name = "jhi_tenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tenant extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "industry", length = 50, nullable = false)
    private Sector industry;

    @NotBlank
    @Column(name = "region", length = 100, nullable = false)
    private String region;

    public Tenant() {}

    public Tenant(String id, String name, Sector industry, String region) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.region = region;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sector getIndustry() {
        return industry;
    }

    public void setIndustry(Sector industry) {
        this.industry = industry;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return id != null && id.equals(((Tenant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Tenant{" +
            "id='" +
            id +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", industry='" +
            industry +
            '\'' +
            ", region='" +
            region +
            '\'' +
            '}'
        );
    }
}
