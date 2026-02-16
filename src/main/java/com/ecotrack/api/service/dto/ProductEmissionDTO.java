package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.ProductEmission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductEmissionDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "CN code is required")
    private String cnCode;

    @NotNull(message = "Quantity is required")
    @DecimalMin("0.0")
    private BigDecimal quantity;

    @NotBlank(message = "Product ID is required")
    private String productId;

    private String productName;

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

    public String getCnCode() {
        return cnCode;
    }

    public void setCnCode(String cnCode) {
        this.cnCode = cnCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductEmissionDTO)) {
            return false;
        }

        ProductEmissionDTO productEmissionDTO = (ProductEmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productEmissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**prettier-ignore*/
    @Override
    public String toString() {
        return (
            "ProductEmissionDTO{" +
            "id=" +
            getId() +
            ", tenantId='" +
            getTenantId() +
            "'" +
            ", cnCode='" +
            getCnCode() +
            "'" +
            ", quantity=" +
            getQuantity() +
            ", productId='" +
            getProductId() +
            "'" +
            ", productName='" +
            getProductName() +
            "'" +
            "}"
        );
    }
}
