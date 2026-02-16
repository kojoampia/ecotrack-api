package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.SupplierType} entity.
 */
public class SupplierTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    private String description;

    @NotBlank
    private String tenantId;

    public SupplierTypeDTO() {}

    public SupplierTypeDTO(Long id, String name, String category, String description, String tenantId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.tenantId = tenantId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SupplierTypeDTO that = (SupplierTypeDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "SupplierTypeDTO{" +
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
