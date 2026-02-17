package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.AuditTrailRepository;
import com.ecotrack.api.service.AuditTrailService;
import com.ecotrack.api.service.dto.AuditTrailDTO;
import com.ecotrack.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.ecopster.web.util.HeaderUtil;
import tech.ecopster.web.util.PaginationUtil;
import tech.ecopster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.AuditTrail}.
 */
@RestController
@RequestMapping("/api/audit-trails")
public class AuditTrailResource {

    private static final Logger log = LoggerFactory.getLogger(AuditTrailResource.class);

    private static final String ENTITY_NAME = "auditTrail";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final AuditTrailService auditTrailService;

    private final AuditTrailRepository auditTrailRepository;

    public AuditTrailResource(AuditTrailService auditTrailService, AuditTrailRepository auditTrailRepository) {
        this.auditTrailService = auditTrailService;
        this.auditTrailRepository = auditTrailRepository;
    }

    /**
     * {@code POST  /audit-trails} : Create a new auditTrail.
     *
     * @param auditTrailDTO the auditTrailDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auditTrailDTO, or with status {@code 400 (Bad Request)} if the auditTrail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<AuditTrailDTO> createAuditTrail(@Valid @RequestBody AuditTrailDTO auditTrailDTO) throws URISyntaxException {
        log.debug("REST request to save AuditTrail : {}", auditTrailDTO);
        if (auditTrailDTO.getId() != null) {
            throw new BadRequestAlertException("A new auditTrail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuditTrailDTO result = auditTrailService.save(auditTrailDTO);
        return ResponseEntity.created(new URI("/api/audit-trails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /audit-trails/:id} : Updates an existing auditTrail.
     *
     * @param id the id of the auditTrailDTO to save
     * @param auditTrailDTO the auditTrailDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditTrailDTO,
     * or with status {@code 400 (Bad Request)} if the auditTrailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditTrailDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuditTrailDTO> updateAuditTrail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuditTrailDTO auditTrailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AuditTrail : {}, {}", id, auditTrailDTO);
        if (auditTrailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditTrailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditTrailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AuditTrailDTO result = auditTrailService.update(auditTrailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, auditTrailDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /audit-trails/:id} : Partial updates given fields of an existing auditTrail, field will ignore if it is null
     *
     * @param id the id of the auditTrailDTO to save
     * @param auditTrailDTO the auditTrailDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auditTrailDTO,
     * or with status {@code 400 (Bad Request)} if the auditTrailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auditTrailDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuditTrailDTO> partialUpdateAuditTrail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AuditTrailDTO auditTrailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AuditTrail partially : {}, {}", id, auditTrailDTO);
        if (auditTrailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auditTrailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auditTrailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuditTrailDTO> result = auditTrailService.partialUpdate(auditTrailDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /audit-trails} : get all the auditTrails.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auditTrails in body
     */
    @GetMapping("")
    public ResponseEntity<List<AuditTrailDTO>> getAllAuditTrails(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AuditTrails");
        Page<AuditTrailDTO> page = auditTrailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /audit-trails/:id} : get the "id" auditTrail.
     *
     * @param id the id of the auditTrailDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auditTrailDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuditTrailDTO> getAuditTrail(@PathVariable("id") Long id) {
        log.debug("REST request to get AuditTrail : {}", id);
        Optional<AuditTrailDTO> auditTrailDTO = auditTrailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auditTrailDTO);
    }

    /**
     * {@code DELETE  /audit-trails/:id} : delete the "id" auditTrail.
     *
     * @param id the id of the auditTrailDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditTrail(@PathVariable("id") Long id) {
        log.debug("REST request to delete AuditTrail : {}", id);
        auditTrailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
