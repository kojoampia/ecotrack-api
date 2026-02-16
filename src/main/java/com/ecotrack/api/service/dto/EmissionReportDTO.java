package com.ecotrack.api.service.dto;

import com.ecotrack.api.service.utils.RecordMetadata;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.ecotrack.api.domain.EmissionReport} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmissionReportDTO implements Serializable {

    private Long id;

    @NotNull(message = "Tenant ID cannot be null")
    private String tenantId;

    @NotNull(message = "Supplier ID cannot be null")
    private String supplierId;

    private RecordMetadata metadata;

    private Instant createdDate;

    private Instant lastModifiedDate;

    public EmissionReportDTO() {}

    public EmissionReportDTO(
        Long id,
        String tenantId,
        String supplierId,
        RecordMetadata metadata,
        Instant createdDate,
        Instant lastModifiedDate
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.supplierId = supplierId;
        this.metadata = metadata;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public RecordMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RecordMetadata metadata) {
        this.metadata = metadata;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionReportDTO)) {
            return false;
        }

        EmissionReportDTO emissionReportDTO = (EmissionReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(emissionReportDTO.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "EmissionReportDTO{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            "'" +
            ", supplierId='" +
            supplierId +
            "'" +
            ", createdDate='" +
            createdDate +
            "'" +
            ", lastModifiedDate='" +
            lastModifiedDate +
            "'" +
            "}"
        );
    }
}
