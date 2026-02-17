package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.EmissionRecordRepository;
import com.ecotrack.api.service.EmissionRecordService;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.EmissionRecord}.
 */
@RestController
@RequestMapping("/api/emission-records")
public class EmissionRecordResource {

    private static final Logger log = LoggerFactory.getLogger(EmissionRecordResource.class);

    private static final String ENTITY_NAME = "emissionRecord";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final EmissionRecordService emissionRecordService;

    private final EmissionRecordRepository emissionRecordRepository;

    public EmissionRecordResource(EmissionRecordService emissionRecordService, EmissionRecordRepository emissionRecordRepository) {
        this.emissionRecordService = emissionRecordService;
        this.emissionRecordRepository = emissionRecordRepository;
    }

    /**
     * {@code POST  /emission-records} : Create a new emission record.
     *
     * @param emissionRecordDTO the emissionRecordDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emissionRecordDTO, or with status {@code 400 (Bad Request)} if the emission record has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<EmissionRecordDTO> createEmissionRecord(@Valid @RequestBody EmissionRecordDTO emissionRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmissionRecord : {}", emissionRecordDTO);
        if (emissionRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new emission record cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmissionRecordDTO result = emissionRecordService.save(emissionRecordDTO);
        return ResponseEntity.created(new URI("/api/emission-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emission-records/:id} : Updates an existing emission record.
     *
     * @param id the id of the emissionRecordDTO to save
     * @param emissionRecordDTO the emissionRecordDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionRecordDTO,
     * or with status {@code 400 (Bad Request)} if the emissionRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmissionRecordDTO> updateEmissionRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmissionRecordDTO emissionRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmissionRecord : {}, {}", id, emissionRecordDTO);
        if (emissionRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmissionRecordDTO result = emissionRecordService.update(emissionRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emissionRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emission-records/:id} : Partial updates given fields of an existing emission record, field will ignore if it is null
     *
     * @param id the id of the emissionRecordDTO to save
     * @param emissionRecordDTO the emissionRecordDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionRecordDTO,
     * or with status {@code 400 (Bad Request)} if the emissionRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmissionRecordDTO> partialUpdateEmissionRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmissionRecordDTO emissionRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmissionRecord partially : {}, {}", id, emissionRecordDTO);
        if (emissionRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmissionRecordDTO> result = emissionRecordService.partialUpdate(emissionRecordDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /emission-records} : get all the emission records.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emission records in body
     */
    @GetMapping("")
    public ResponseEntity<List<EmissionRecordDTO>> getAllEmissionRecords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmissionRecords");
        Page<EmissionRecordDTO> page = emissionRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emission-records/tenant/:tenantId} : get emission records for a tenant.
     *
     * @param tenantId the tenant ID
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emission records in body
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<EmissionRecordDTO>> getEmissionRecordsByTenant(
        @PathVariable("tenantId") String tenantId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EmissionRecords for tenant: {}", tenantId);
        Page<EmissionRecordDTO> page = emissionRecordService.findByTenantId(tenantId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emission-records/:id} : get the "id" emission record.
     *
     * @param id the id of the emissionRecordDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emissionRecordDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmissionRecordDTO> getEmissionRecord(@PathVariable("id") Long id) {
        log.debug("REST request to get EmissionRecord : {}", id);
        Optional<EmissionRecordDTO> emissionRecordDTO = emissionRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emissionRecordDTO);
    }

    /**
     * {@code DELETE  /emission-records/:id} : delete the "id" emission record.
     *
     * @param id the id of the emissionRecordDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmissionRecord(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmissionRecord : {}", id);
        emissionRecordService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
