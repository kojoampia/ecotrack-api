package com.ecotrack.api.domain;

import com.ecotrack.api.domain.enumeration.EvidenceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmissionEvidence.
 */
@Entity
@Table(name = "emission_evidence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmissionEvidence extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "supplier_id", length = 100)
    private String supplierId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "evidence_type", nullable = false, length = 50)
    private EvidenceType evidenceType;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_path", columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;

    @Column(name = "retention_period")
    private Integer retentionPeriod;

    @Column(name = "legal_reference", columnDefinition = "TEXT")
    private String legalReference;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private EmissionReport report;

    public EmissionEvidence() {}

    public EmissionEvidence(String tenantId, EvidenceType evidenceType) {
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

    public EmissionReport getReport() {
        return report;
    }

    public void setReport(EmissionReport report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionEvidence)) {
            return false;
        }
        return id != null && id.equals(((EmissionEvidence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "EmissionEvidence{" +
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
