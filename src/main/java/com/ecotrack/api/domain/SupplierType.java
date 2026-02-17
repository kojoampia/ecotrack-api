package com.ecotrack.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SupplierType.
 */
@Entity
@Table(name = "eco_supplier_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SupplierType extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    // Constructors
    public SupplierType() {}

    public SupplierType(String name, String category, String tenantId) {
        this.name = name;
        this.category = category;
        this.tenantId = tenantId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierType)) {
            return false;
        }
        return id != null && id.equals(((SupplierType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "SupplierType{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", category='" +
            category +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", tenantId='" +
            tenantId +
            '\'' +
            '}'
        );
    }
}
