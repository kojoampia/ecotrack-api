package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.EmissionReportRepository;
import com.ecotrack.api.service.EmissionReportService;
import com.ecotrack.api.service.dto.EmissionReportDTO;
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
import tech.ecopster.web.util.HeaderUtil;
import tech.ecopster.web.util.PaginationUtil;
import tech.ecopster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.EmissionReport}.
 */
@RestController
@RequestMapping("/api/emission-reports")
public class EmissionReportResource {

    private static final Logger log = LoggerFactory.getLogger(EmissionReportResource.class);

    private static final String ENTITY_NAME = "emissionReport";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final EmissionReportService emissionReportService;

    private final EmissionReportRepository emissionReportRepository;

    public EmissionReportResource(EmissionReportService emissionReportService, EmissionReportRepository emissionReportRepository) {
        this.emissionReportService = emissionReportService;
        this.emissionReportRepository = emissionReportRepository;
    }

    /**
     * {@code POST  /emission-reports} : Create a new emissionReport.
     *
     * @param emissionReportDTO the emissionReportDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emissionReportDTO, or with status {@code 400 (Bad Request)} if the emissionReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<EmissionReportDTO> createEmissionReport(@Valid @RequestBody EmissionReportDTO emissionReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmissionReport : {}", emissionReportDTO);
        if (emissionReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new emissionReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmissionReportDTO result = emissionReportService.save(emissionReportDTO);
        return ResponseEntity.created(new URI("/api/emission-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emission-reports/:id} : Updates an existing emissionReport.
     *
     * @param id the id of the emissionReportDTO to save
     * @param emissionReportDTO the emissionReportDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionReportDTO,
     * or with status {@code 400 (Bad Request)} if the emissionReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmissionReportDTO> updateEmissionReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmissionReportDTO emissionReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmissionReport : {}, {}", id, emissionReportDTO);
        if (emissionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmissionReportDTO result = emissionReportService.update(emissionReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emissionReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emission-reports/:id} : Partial updates given fields of an existing emissionReport, field will ignore if it is null
     *
     * @param id the id of the emissionReportDTO to save
     * @param emissionReportDTO the emissionReportDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emissionReportDTO,
     * or with status {@code 400 (Bad Request)} if the emissionReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emissionReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmissionReportDTO> partialUpdateEmissionReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmissionReportDTO emissionReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmissionReport partially : {}, {}", id, emissionReportDTO);
        if (emissionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emissionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emissionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmissionReportDTO> result = emissionReportService.partialUpdate(emissionReportDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /emission-reports} : get all the emissionReports.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emissionReports in body
     */
    @GetMapping("")
    public ResponseEntity<List<EmissionReportDTO>> getAllEmissionReports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmissionReports");
        Page<EmissionReportDTO> page = emissionReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emission-reports/:id} : get the "id" emissionReport.
     *
     * @param id the id of the emissionReportDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emissionReportDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmissionReportDTO> getEmissionReport(@PathVariable("id") Long id) {
        log.debug("REST request to get EmissionReport : {}", id);
        Optional<EmissionReportDTO> emissionReportDTO = emissionReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emissionReportDTO);
    }

    /**
     * {@code DELETE  /emission-reports/:id} : delete the "id" emissionReport.
     *
     * @param id the id of the emissionReportDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmissionReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmissionReport : {}", id);
        emissionReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
