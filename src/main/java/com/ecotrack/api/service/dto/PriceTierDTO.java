package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ecotrack.api.domain.PriceTier} entity.
 */
public class PriceTierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    private String badge;

    private BigDecimal price;

    private String currency;

    private String billingCycle;

    private Set<String> features;

    public PriceTierDTO() {}

    public PriceTierDTO(Long id, String name, String badge, BigDecimal price, String currency, String billingCycle, Set<String> features) {
        this.id = id;
        this.name = name;
        this.badge = badge;
        this.price = price;
        this.currency = currency;
        this.billingCycle = billingCycle;
        this.features = features;
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

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceTierDTO that = (PriceTierDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "PriceTierDTO{" +
            "id=" +
            id +
            ", name='" +
            name +
            '\'' +
            ", badge='" +
            badge +
            '\'' +
            ", price=" +
            price +
            ", currency='" +
            currency +
            '\'' +
            ", billingCycle='" +
            billingCycle +
            '\'' +
            ", features=" +
            features +
            '}'
        );
    }
}
