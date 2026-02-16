package com.ecotrack.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "jhi_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank
    @Column(name = "sku", nullable = false, length = 100, unique = true)
    private String sku;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "unit_of_measure", length = 50)
    private String unitOfMeasure;

    @Column(name = "total_carbon_footprint", precision = 21, scale = 2)
    private BigDecimal totalCarbonFootprint;

    @Column(name = "product_created_date")
    private Instant productCreatedDate;

    @Column(name = "product_last_modified_date")
    private Instant productLastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Supplier supplier;

    // Constructors
    public Product() {}

    public Product(String name, String sku) {
        this.name = name;
        this.sku = sku;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setProductLastModifiedDate(Instant productLastModifiedDate) {
        this.productLastModifiedDate = productLastModifiedDate;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "Product{" +
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
            ", unitOfMeasure='" +
            unitOfMeasure +
            '\'' +
            ", totalCarbonFootprint=" +
            totalCarbonFootprint +
            '}'
        );
    }
}
