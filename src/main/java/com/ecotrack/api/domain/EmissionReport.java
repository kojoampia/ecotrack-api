package com.ecotrack.api.domain;

import com.ecotrack.api.domain.converter.RecordMetadataConverter;
import com.ecotrack.api.service.utils.RecordMetadata;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * EmissionReport entity for storing emission report data.
 */
@Entity
@Table(name = "emission_report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmissionReport extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    @NotNull
    @Column(name = "supplier_id", nullable = false, length = 100)
    private String supplierId;

    @Convert(converter = RecordMetadataConverter.class)
    @Column(name = "metadata", columnDefinition = "TEXT")
    private RecordMetadata metadata;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmissionRecord> emissionRecords = new HashSet<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmissionEvidence> emissionEvidenceFiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmissionReport id(Long id) {
        this.setId(id);
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public EmissionReport tenantId(String tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public EmissionReport supplierId(String supplierId) {
        this.setSupplierId(supplierId);
        return this;
    }

    public RecordMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RecordMetadata metadata) {
        this.metadata = metadata;
    }

    public EmissionReport metadata(RecordMetadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public Set<EmissionRecord> getEmissionRecords() {
        return emissionRecords;
    }

    public void setEmissionRecords(Set<EmissionRecord> emissionRecords) {
        this.emissionRecords = emissionRecords;
    }

    public EmissionReport emissionRecords(Set<EmissionRecord> emissionRecords) {
        this.setEmissionRecords(emissionRecords);
        return this;
    }

    public EmissionReport addEmissionRecords(EmissionRecord emissionRecord) {
        this.emissionRecords.add(emissionRecord);
        emissionRecord.setReport(this);
        return this;
    }

    public EmissionReport removeEmissionRecords(EmissionRecord emissionRecord) {
        this.emissionRecords.remove(emissionRecord);
        emissionRecord.setReport(null);
        return this;
    }

    public Set<EmissionEvidence> getEmissionEvidenceFiles() {
        return emissionEvidenceFiles;
    }

    public void setEmissionEvidenceFiles(Set<EmissionEvidence> emissionEvidenceFiles) {
        this.emissionEvidenceFiles = emissionEvidenceFiles;
    }

    public EmissionReport emissionEvidenceFiles(Set<EmissionEvidence> emissionEvidenceFiles) {
        this.setEmissionEvidenceFiles(emissionEvidenceFiles);
        return this;
    }

    public EmissionReport addEmissionEvidenceFiles(EmissionEvidence emissionEvidence) {
        this.emissionEvidenceFiles.add(emissionEvidence);
        emissionEvidence.setReport(this);
        return this;
    }

    public EmissionReport removeEmissionEvidenceFiles(EmissionEvidence emissionEvidence) {
        this.emissionEvidenceFiles.remove(emissionEvidence);
        emissionEvidence.setReport(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionReport)) {
            return false;
        }
        return getId() != null && getId().equals(((EmissionReport) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EmissionReport{" + "id=" + getId() + ", tenantId='" + getTenantId() + "'" + ", supplierId='" + getSupplierId() + "'" + "}";
    }
}
