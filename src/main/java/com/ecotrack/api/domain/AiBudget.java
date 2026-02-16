package com.ecotrack.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An AiBudget.
 */
@Entity
@Table(name = "jhi_ai_budget")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AiBudget extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "consumed", precision = 21, scale = 2)
    private BigDecimal consumed;

    @Column(name = "last_purchased")
    private LocalDate lastPurchased;

    @Column(name = "purchased", precision = 21, scale = 2)
    private BigDecimal purchased;

    public AiBudget() {}

    public AiBudget(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConsumed() {
        return consumed;
    }

    public void setConsumed(BigDecimal consumed) {
        this.consumed = consumed;
    }

    public LocalDate getLastPurchased() {
        return lastPurchased;
    }

    public void setLastPurchased(LocalDate lastPurchased) {
        this.lastPurchased = lastPurchased;
    }

    public BigDecimal getPurchased() {
        return purchased;
    }

    public void setPurchased(BigDecimal purchased) {
        this.purchased = purchased;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AiBudget)) {
            return false;
        }
        return id != null && id.equals(((AiBudget) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "AiBudget{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", amount=" +
            amount +
            ", consumed=" +
            consumed +
            ", lastPurchased=" +
            lastPurchased +
            ", purchased=" +
            purchased +
            '}'
        );
    }
}
