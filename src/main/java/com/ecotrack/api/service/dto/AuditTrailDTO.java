package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.AuditAction;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.ecotrack.api.domain.AuditTrail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuditTrailDTO implements Serializable {

    private Long id;

    @NotNull(message = "Tenant ID cannot be null")
    private String tenantId;

    @NotNull(message = "Action cannot be null")
    private AuditAction action;

    @NotNull(message = "Entity type cannot be null")
    private String entityType;

    @NotNull(message = "Entity ID cannot be null")
    private Long entityId;

    private String oldValue;

    private String newValue;

    private String changedBy;

    private Instant changedAt;

    private String ipAddress;

    private String userAgent;

    private String reason;

    private Instant createdDate;

    private Instant lastModifiedDate;

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

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(o instanceof AuditTrailDTO)) {
            return false;
        }

        AuditTrailDTO auditTrailDTO = (AuditTrailDTO) o;
        if (this.id == null) {
            return false;
        }
        return this.id.equals(auditTrailDTO.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "AuditTrailDTO{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            "'" +
            ", action='" +
            action +
            "'" +
            ", entityType='" +
            entityType +
            "'" +
            ", entityId=" +
            entityId +
            ", changedAt='" +
            changedAt +
            "'" +
            "}"
        );
    }
}
