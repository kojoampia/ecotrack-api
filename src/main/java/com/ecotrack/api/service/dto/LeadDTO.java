package com.ecotrack.api.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ecotrack.api.domain.Lead} entity.
 */
public class LeadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Email
    private String email;

    private String source;

    private String notes;

    private Boolean contacted;

    private Instant submittedAt;

    public LeadDTO() {}

    public LeadDTO(String email) {
        this.email = email;
    }

    public LeadDTO(String email, String source) {
        this.email = email;
        this.source = source;
    }

    public LeadDTO(String email, String source, Instant timestamp) {
        this.email = email;
        this.source = source;
        this.submittedAt = timestamp;
    }

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
        if (!(o instanceof LeadDTO)) {
            return false;
        }
        LeadDTO leadDTO = (LeadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "LeadDTO{" +
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
