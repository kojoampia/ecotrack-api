package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Installation} entity.
 */
public class InstallationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "Installation name cannot be null")
    private String installationName;

    @NotNull(message = "Country cannot be null")
    private String country;

    private String unlocode;

    private String tenantId;

    private Long supplierId;

    private String supplierCompanyName;

    private Instant createdDate;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstallationName() {
        return installationName;
    }

    public void setInstallationName(String installationName) {
        this.installationName = installationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnlocode() {
        return unlocode;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierCompanyName() {
        return supplierCompanyName;
    }

    public void setSupplierCompanyName(String supplierCompanyName) {
        this.supplierCompanyName = supplierCompanyName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstallationDTO)) {
            return false;
        }

        InstallationDTO installationDTO = (InstallationDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(installationDTO.id);
    }

    @Override
    public int hashCode() {
        return 31 * 31 + (id == null ? 0 : id.hashCode());
    }

    @Override
    public String toString() {
        return (
            "InstallationDTO{" +
            "id=" +
            id +
            ", installationName='" +
            installationName +
            '\'' +
            ", country='" +
            country +
            '\'' +
            ", unlocode='" +
            unlocode +
            '\'' +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", supplierId=" +
            supplierId +
            ", supplierCompanyName='" +
            supplierCompanyName +
            '\'' +
            '}'
        );
    }
}
