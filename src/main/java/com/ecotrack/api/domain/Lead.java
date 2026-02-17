package com.ecotrack.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lead entity for collecting email addresses of interested users.
 */
@Entity
@Table(name = "eco_lead")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lead extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "contacted")
    private Boolean contacted = false;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    // Constructors
    public Lead() {
        this.submittedAt = Instant.now();
    }

    public Lead(String email) {
        this.email = email;
    }

    public Lead(String email, String source) {
        this.email = email;
        this.source = source;
    }

    // Getters and Setters
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getContacted() {
        return contacted;
    }

    public void setContacted(Boolean contacted) {
        this.contacted = contacted;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lead)) {
            return false;
        }
        return getId() != null && getId().equals(((Lead) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Lead{" +
            "id=" +
            getId() +
            ", email='" +
            getEmail() +
            "'" +
            ", source='" +
            getSource() +
            "'" +
            ", contacted=" +
            getContacted() +
            ", submittedAt=" +
            getSubmittedAt() +
            "}"
        );
    }
}
