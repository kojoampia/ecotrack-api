package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.AiBudget} entity.
 */
public class AiBudgetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String tenantId;

    private BigDecimal amount;

    private BigDecimal consumed;

    private LocalDate lastPurchased;

    private BigDecimal purchased;

    public AiBudgetDTO() {}

    public AiBudgetDTO(Long id, String tenantId, BigDecimal amount, BigDecimal consumed, LocalDate lastPurchased, BigDecimal purchased) {
        this.id = id;
        this.tenantId = tenantId;
        this.amount = amount;
        this.consumed = consumed;
        this.lastPurchased = lastPurchased;
        this.purchased = purchased;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AiBudgetDTO that = (AiBudgetDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return (
            "AiBudgetDTO{" +
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
