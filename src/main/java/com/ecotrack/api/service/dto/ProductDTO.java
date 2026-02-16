package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String sku;

    private String description;

    private String category;

    private String unitOfMeasure;

    private BigDecimal totalCarbonFootprint;

    private Instant productCreatedDate;

    private Instant productLastModifiedDate;

    public ProductDTO() {}

    public ProductDTO(Long id, String name, String sku) {
        this.id = id;
        this.name = name;
        this.sku = sku;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getTotalCarbonFootprint() {
        return totalCarbonFootprint;
    }

    public void setTotalCarbonFootprint(BigDecimal totalCarbonFootprint) {
        this.totalCarbonFootprint = totalCarbonFootprint;
    }

    public Instant getProductCreatedDate() {
        return productCreatedDate;
    }

    public void setProductCreatedDate(Instant productCreatedDate) {
        this.productCreatedDate = productCreatedDate;
    }

    public Instant getProductLastModifiedDate() {
        return productLastModifiedDate;
    }

    public void setProductLastModifiedDate(Instant productLastModifiedDate) {
        this.productLastModifiedDate = productLastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "ProductDTO{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", sku='" +
            sku +
            '\'' +
            ", category='" +
            category +
            '\'' +
            ", totalCarbonFootprint=" +
            totalCarbonFootprint +
            '}'
        );
    }
}
