package com.ecotrack.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductEmission.
 */
@Entity
@Table(name = "eco_product_emission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductEmission extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @NotBlank
    @Column(name = "cn_code", nullable = false, length = 20)
    private String cnCode;

    @Column(name = "quantity", nullable = false, precision = 21, scale = 2)
    private BigDecimal quantity;

    @NotBlank
    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "product_name", length = 255)
    private String productName;

    // Constructors
    public ProductEmission() {}

    public ProductEmission(String tenantId, String cnCode, BigDecimal quantity, String productId) {
        this.tenantId = tenantId;
        this.cnCode = cnCode;
        this.quantity = quantity;
        this.productId = productId;
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
        if (!(o instanceof ProductEmission)) {
            return false;
        }
        return id != null && id.equals(((ProductEmission) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**prettier-ignore*/
    @Override
    public String toString() {
        return (
            "ProductEmission{" +
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
