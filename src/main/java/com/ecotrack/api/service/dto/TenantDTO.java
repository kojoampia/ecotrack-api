package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.Sector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Tenant} entity.
 */
public class TenantDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank
    private String name;

    @NotNull
    private Sector industry;

    @NotBlank
    private String region;

    public TenantDTO() {}

    public TenantDTO(String id, String name, Sector industry, String region) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.region = region;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantDTO that = (TenantDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "TenantDTO{" +
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
