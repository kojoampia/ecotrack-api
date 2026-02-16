package com.ecotrack.api.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PriceTier.
 */
@Entity
@Table(name = "jhi_price_tier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PriceTier extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "badge", length = 120)
    private String badge;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", length = 20)
    private String currency;

    @Column(name = "billing_cycle", length = 50)
    private String billingCycle;

    @ElementCollection
    @CollectionTable(name = "jhi_price_tier_features", joinColumns = @JoinColumn(name = "price_tier_id"))
    @Column(name = "feature", length = 255)
    private Set<String> features = new HashSet<>();

    public PriceTier() {}

    @Override
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
        if (!(o instanceof PriceTier)) {
            return false;
        }
        return id != null && id.equals(((PriceTier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "PriceTier{" +
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
