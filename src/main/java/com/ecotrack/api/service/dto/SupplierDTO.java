package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Supplier} entity.
 */
public class SupplierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String companyName;

    @NotBlank
    @Email
    private String contactEmail;

    private String industry;

    private Integer tier;

    private String address;

    private String phone;

    private String website;

    @NotBlank
    private String tenantId;

    private Long supplierTypeId;

    private String supplierTypeName;

    public SupplierDTO() {}

    public SupplierDTO(Long id, String companyName, String contactEmail, String tenantId) {
        this.id = id;
        this.companyName = companyName;
        this.contactEmail = contactEmail;
        this.tenantId = tenantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSupplierTypeId() {
        return supplierTypeId;
    }

    public void setSupplierTypeId(Long supplierTypeId) {
        this.supplierTypeId = supplierTypeId;
    }

    public String getSupplierTypeName() {
        return supplierTypeName;
    }

    public void setSupplierTypeName(String supplierTypeName) {
        this.supplierTypeName = supplierTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierDTO that = (SupplierDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "SupplierDTO{" +
            "id=" +
            id +
            ", companyName='" +
            companyName +
            '\'' +
            ", contactEmail='" +
            contactEmail +
            '\'' +
            ", industry='" +
            industry +
            '\'' +
            ", tier=" +
            tier +
            ", tenantId='" +
            tenantId +
            '\'' +
            '}'
        );
    }
}
