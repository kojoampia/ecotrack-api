package com.ecotrack.api.domain;

import com.ecotrack.api.domain.enumeration.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Importer.
 */
@Entity
@Table(name = "eco_importer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Importer extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @NotBlank
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "eori_number", length = 20)
    private String eoriNumber;

    @Column(name = "vat_number", length = 50)
    private String vatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "sector", length = 50)
    private Sector sector;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "contact_name", length = 255)
    private String contactName;

    @Email
    @Column(name = "contact_email", length = 254)
    private String contactEmail;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "authorized_representative", length = 255)
    private String authorizedRepresentative;

    @Column(name = "estimated_annual_import_volume", precision = 21, scale = 2)
    private BigDecimal estimatedAnnualImportVolume;

    @Column(name = "agreed_to_terms")
    private Boolean agreedToTerms;

    // Constructors
    public Importer() {}

    public Importer(String tenantId, String companyName) {
        this.tenantId = tenantId;
        this.companyName = companyName;
    }

    // Getters and Setters
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
        if (!(o instanceof Importer)) {
            return false;
        }
        return id != null && id.equals(((Importer) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Importer{" +
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
