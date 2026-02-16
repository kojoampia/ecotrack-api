package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.Sector;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Importer} entity.
 */
public class ImporterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String tenantId;

    @NotBlank
    private String companyName;

    private String eoriNumber;

    private String vatNumber;

    private Sector sector;

    private String country;

    private String address;

    private String contactName;

    @Email
    private String contactEmail;

    private String contactPhone;

    private String authorizedRepresentative;

    private BigDecimal estimatedAnnualImportVolume;

    private Boolean agreedToTerms;

    public ImporterDTO() {}

    public ImporterDTO(Long id, String tenantId, String companyName) {
        this.id = id;
        this.tenantId = tenantId;
        this.companyName = companyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEoriNumber() {
        return eoriNumber;
    }

    public void setEoriNumber(String eoriNumber) {
        this.eoriNumber = eoriNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAuthorizedRepresentative() {
        return authorizedRepresentative;
    }

    public void setAuthorizedRepresentative(String authorizedRepresentative) {
        this.authorizedRepresentative = authorizedRepresentative;
    }

    public BigDecimal getEstimatedAnnualImportVolume() {
        return estimatedAnnualImportVolume;
    }

    public void setEstimatedAnnualImportVolume(BigDecimal estimatedAnnualImportVolume) {
        this.estimatedAnnualImportVolume = estimatedAnnualImportVolume;
    }

    public Boolean getAgreedToTerms() {
        return agreedToTerms;
    }

    public void setAgreedToTerms(Boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImporterDTO that = (ImporterDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "ImporterDTO{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", companyName='" +
            companyName +
            '\'' +
            ", country='" +
            country +
            '\'' +
            ", sector=" +
            sector +
            '}'
        );
    }
}
