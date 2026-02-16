package com.ecotrack.api.service.dto;

import com.ecotrack.api.domain.enumeration.EvidenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.ecotrack.api.domain.EmissionEvidence} entity.
 */
public class EmissionEvidenceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String tenantId;

    private String supplierId;

    @NotNull
    private EvidenceType evidenceType;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String mimeType;

    private String checksum;

    private Instant uploadedAt;

    private Boolean verified;

    private String verificationNotes;

    private Integer retentionPeriod;

    private String legalReference;

    public EmissionEvidenceDTO() {}

    public EmissionEvidenceDTO(String tenantId, EvidenceType evidenceType) {
        this.tenantId = tenantId;
        this.evidenceType = evidenceType;
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

    public EvidenceType getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(EvidenceType evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerificationNotes() {
        return verificationNotes;
    }

    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }

    public Integer getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(Integer retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public String getLegalReference() {
        return legalReference;
    }

    public void setLegalReference(String legalReference) {
        this.legalReference = legalReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionEvidenceDTO)) {
            return false;
        }
        return id != null && id.equals(((EmissionEvidenceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "EmissionEvidenceDTO{" +
            "id=" +
            id +
            ", tenantId='" +
            tenantId +
            '\'' +
            ", supplierId='" +
            supplierId +
            '\'' +
            ", evidenceType=" +
            evidenceType +
            ", fileName='" +
            fileName +
            '\'' +
            ", uploadedAt=" +
            uploadedAt +
            ", verified=" +
            verified +
            '}'
        );
    }
}
