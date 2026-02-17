package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.EmissionEvidenceRepository;
import com.ecotrack.api.service.EmissionEvidenceService;
import com.ecotrack.api.service.dto.EmissionEvidenceDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.EmissionEvidence}.
 */
@RestController
@RequestMapping("/api/emission-evidences")
public class EmissionEvidenceResource {

    private static final Logger log = LoggerFactory.getLogger(EmissionEvidenceResource.class);

    private static final String ENTITY_NAME = "emissionEvidence";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final EmissionEvidenceService emissionEvidenceService;

    private final EmissionEvidenceRepository emissionEvidenceRepository;

    public EmissionEvidenceResource(
        EmissionEvidenceService emissionEvidenceService,
        EmissionEvidenceRepository emissionEvidenceRepository
    ) {
        this.emissionEvidenceService = emissionEvidenceService;
        this.emissionEvidenceRepository = emissionEvidenceRepository;
    }

    /**
     * {@code POST  /emission-evidences} : Create a new emission evidence.
     *
     * @param emissionEvidenceDTO the emissionEvidenceDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emissionEvidenceDTO,
     * or with status {@code 400 (Bad Request)} if the emission evidence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<EmissionEvidenceDTO> createEmissionEvidence(@Valid @RequestBody EmissionEvidenceDTO emissionEvidenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmissionEvidence : {}", emissionEvidenceDTO);
        if (emissionEvidenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new emission evidence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmissionEvidenceDTO result = emissionEvidenceService.save(emissionEvidenceDTO);
        return ResponseEntity.created(new URI("/api/emission-evidences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emission-evidences/:id} : Updates an existing emission evidence.
     *
     * @param id the id of the emissionEvidenceDTO to save
     * @param emissionEvidenceDTO the emissionEvidenceDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionEvidenceDTO,
     * or with status {@code 400 (Bad Request)} if the emissionEvidenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionEvidenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmissionEvidenceDTO> updateEmissionEvidence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmissionEvidenceDTO emissionEvidenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmissionEvidence : {}, {}", id, emissionEvidenceDTO);
        if (emissionEvidenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionEvidenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionEvidenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmissionEvidenceDTO result = emissionEvidenceService.update(emissionEvidenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emissionEvidenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emission-evidences/:id} : Partial updates given fields of an existing emission evidence, field will ignore if it is null
     *
     * @param id the id of the emissionEvidenceDTO to save
     * @param emissionEvidenceDTO the emissionEvidenceDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionEvidenceDTO,
     * or with status {@code 400 (Bad Request)} if the emissionEvidenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionEvidenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmissionEvidenceDTO> partialUpdateEmissionEvidence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmissionEvidenceDTO emissionEvidenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmissionEvidence partially : {}, {}", id, emissionEvidenceDTO);
        if (emissionEvidenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionEvidenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionEvidenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmissionEvidenceDTO> result = emissionEvidenceService.partialUpdate(emissionEvidenceDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /emission-evidences} : get all the emission evidence.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emission evidence in body
     */
    @GetMapping("")
    public ResponseEntity<List<EmissionEvidenceDTO>> getAllEmissionEvidence(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmissionEvidence");
        Page<EmissionEvidenceDTO> page = emissionEvidenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emission-evidences/:id} : get the "id" emission evidence.
     *
     * @param id the id of the emissionEvidenceDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emissionEvidenceDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmissionEvidenceDTO> getEmissionEvidence(@PathVariable("id") Long id) {
        log.debug("REST request to get EmissionEvidence : {}", id);
        Optional<EmissionEvidenceDTO> emissionEvidenceDTO = emissionEvidenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emissionEvidenceDTO);
    }

    /**
     * {@code DELETE  /emission-evidences/:id} : delete the "id" emission evidence.
     *
     * @param id the id of the emissionEvidenceDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmissionEvidence(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmissionEvidence : {}", id);
        emissionEvidenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
