package com.ecotrack.api.web.rest;

import com.ecotrack.api.service.LeadService;
import com.ecotrack.api.service.dto.LeadDTO;
import com.ecotrack.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.Lead}.
 */
@RestController
@RequestMapping("/api")
public class LeadResource {

    private static final Logger log = LoggerFactory.getLogger(LeadResource.class);

    private static final String ENTITY_NAME = "lead";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final LeadService leadService;

    public LeadResource(LeadService leadService) {
        this.leadService = leadService;
    }

    /**
     * {@code POST  /notify-me} : Register a new lead email address.
     * This endpoint is publicly accessible for users to sign up for notifications.
     *
     * @param leadDTO the leadDTO containing the email to register
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leadDTO,
     * or with status {@code 200 (OK)} if the email already exists
     */
    @PostMapping("/notify-me")
    public ResponseEntity<Map<String, Object>> notifyMe(@Valid @RequestBody LeadDTO leadDTO) {
        log.debug("REST request to register email for notifications : {}", leadDTO.getEmail());

        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        if (leadService.existsByEmail(leadDTO.getEmail())) {
            log.debug("Email already registered: {}", leadDTO.getEmail());
            response.put("message", "You're already on our list! We'll notify you when we launch.");
            response.put("status", "existing");
            response.put("email", leadDTO.getEmail());
            return ResponseEntity.ok(response);
        }

        // Save the new lead
        LeadDTO result = leadService.save(leadDTO);

        log.info("New lead registered: {}", result.getEmail());
        response.put("message", "Thank you! We'll notify you when we launch.");
        response.put("status", "success");
        response.put("email", result.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * {@code POST  /leads} : Create a new lead (admin only).
     *
     * @param leadDTO the leadDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leadDTO,
     * or with status {@code 400 (Bad Request)} if the lead has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leads")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LeadDTO> createLead(@Valid @RequestBody LeadDTO leadDTO) throws URISyntaxException {
        log.debug("REST request to save Lead : {}", leadDTO);
        if (leadDTO.getId() != null) {
            throw new BadRequestAlertException("A new lead cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (leadService.existsByEmail(leadDTO.getEmail())) {
            throw new BadRequestAlertException("Email already exists", ENTITY_NAME, "emailexists");
        }

        LeadDTO result = leadService.save(leadDTO);
        return ResponseEntity.created(new URI("/api/leads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leads/:id} : get the "id" lead (admin only).
     *
     * @param id the id of the leadDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leadDTO,
     * or with status {@code 404 (Not Found)}
     */
    @GetMapping("/leads/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LeadDTO> getLead(@PathVariable Long id) {
        log.debug("REST request to get Lead : {}", id);
        return leadService.findOne(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /leads/:id} : delete the "id" lead (admin only).
     *
     * @param id the id of the leadDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}
     */
    @DeleteMapping("/leads/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        log.debug("REST request to delete Lead : {}", id);
        leadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
