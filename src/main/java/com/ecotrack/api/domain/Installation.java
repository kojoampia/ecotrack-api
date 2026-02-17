package com.ecotrack.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Installation entity for storing installation information
 */
@Entity
@Table(name = "installation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Installation extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "installation_name", nullable = false)
    private String installationName;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "unlocode")
    private String unlocode;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "installations", "products" }, allowSetters = true)
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Installation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstallationName() {
        return this.installationName;
    }

    public Installation installationName(String installationName) {
        this.setInstallationName(installationName);
        return this;
    }

    public void setInstallationName(String installationName) {
        this.installationName = installationName;
    }

    public String getCountry() {
        return this.country;
    }

    public Installation country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnlocode() {
        return this.unlocode;
    }

    public Installation unlocode(String unlocode) {
        this.setUnlocode(unlocode);
        return this;
    }

    public void setUnlocode(String unlocode) {
        this.unlocode = unlocode;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public Installation tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public Installation supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Installation)) {
            return false;
        }
        return id != null && id.equals(((Installation) o).id);
    }

    @Override
    public int hashCode() {
        return 31 * 31 + (id == null ? 0 : id.hashCode());
    }

    @Override
    public String toString() {
        return (
            "Installation{" +
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
            '}'
        );
    }
}
