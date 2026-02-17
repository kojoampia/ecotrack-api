package com.ecotrack.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Supplier.
 */
@Entity
@Table(name = "eco_supplier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Supplier extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @NotBlank
    @Email
    @Column(name = "contact_email", nullable = false, length = 254)
    private String contactEmail;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "tier")
    private Integer tier;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "website", length = 255)
    private String website;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_type_id")
    @JsonIgnoreProperties(value = { "suppliers" }, allowSetters = true)
    private SupplierType supplierType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplier" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplier" }, allowSetters = true)
    private Set<Installation> installations = new HashSet<>();

    // Constructors
    public Supplier() {}

    public Supplier(String companyName, String contactEmail, String tenantId) {
        this.companyName = companyName;
        this.contactEmail = contactEmail;
        this.tenantId = tenantId;
    }

    // Getters and Setters
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

    public SupplierType getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(SupplierType supplierType) {
        this.supplierType = supplierType;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Supplier addProducts(Product product) {
        this.products.add(product);
        product.setSupplier(this);
        return this;
    }

    public Supplier removeProducts(Product product) {
        this.products.remove(product);
        product.setSupplier(null);
        return this;
    }

    public Set<Installation> getInstallations() {
        return this.installations;
    }

    public void setInstallations(Set<Installation> installations) {
        this.installations = installations;
    }

    public Supplier addInstallations(Installation installation) {
        this.installations.add(installation);
        installation.setSupplier(this);
        return this;
    }

    public Supplier removeInstallations(Installation installation) {
        this.installations.remove(installation);
        installation.setSupplier(null);
        return this;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Supplier)) {
            return false;
        }
        return id != null && id.equals(((Supplier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Supplier{" +
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
