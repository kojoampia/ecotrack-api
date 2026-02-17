package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.ComplianceReportRepository;
import com.ecotrack.api.service.ComplianceReportService;
import com.ecotrack.api.service.dto.ComplianceReportDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.ComplianceReport}.
 */
@RestController
@RequestMapping("/api/compliance-reports")
public class ComplianceReportResource {

    private static final Logger log = LoggerFactory.getLogger(ComplianceReportResource.class);

    private static final String ENTITY_NAME = "complianceReport";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final ComplianceReportService complianceReportService;

    private final ComplianceReportRepository complianceReportRepository;

    public ComplianceReportResource(
        ComplianceReportService complianceReportService,
        ComplianceReportRepository complianceReportRepository
    ) {
        this.complianceReportService = complianceReportService;
        this.complianceReportRepository = complianceReportRepository;
    }

    /**
     * {@code POST  /compliance-reports} : Create a new complianceReport.
     *
     * @param complianceReportDTO the complianceReportDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new complianceReportDTO, or with status {@code 400 (Bad Request)} if the complianceReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<ComplianceReportDTO> createComplianceReport(@Valid @RequestBody ComplianceReportDTO complianceReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save ComplianceReport : {}", complianceReportDTO);
        if (complianceReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new complianceReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComplianceReportDTO result = complianceReportService.save(complianceReportDTO);
        return ResponseEntity.created(new URI("/api/compliance-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compliance-reports/:id} : Updates an existing complianceReport.
     *
     * @param id the id of the complianceReportDTO to save
     * @param complianceReportDTO the complianceReportDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complianceReportDTO,
     * or with status {@code 400 (Bad Request)} if the complianceReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complianceReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComplianceReportDTO> updateComplianceReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ComplianceReportDTO complianceReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ComplianceReport : {}, {}", id, complianceReportDTO);
        if (complianceReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complianceReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complianceReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComplianceReportDTO result = complianceReportService.update(complianceReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, complianceReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compliance-reports/:id} : Partial updates given fields of an existing complianceReport, field will ignore if it is null
     *
     * @param id the id of the complianceReportDTO to save
     * @param complianceReportDTO the complianceReportDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated complianceReportDTO,
     * or with status {@code 400 (Bad Request)} if the complianceReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the complianceReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComplianceReportDTO> partialUpdateComplianceReport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComplianceReportDTO complianceReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ComplianceReport partially : {}, {}", id, complianceReportDTO);
        if (complianceReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, complianceReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!complianceReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComplianceReportDTO> result = complianceReportService.partialUpdate(complianceReportDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /compliance-reports} : get all the complianceReports.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of complianceReports in body
     */
    @GetMapping("")
    public ResponseEntity<List<ComplianceReportDTO>> getAllComplianceReports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ComplianceReports");
        Page<ComplianceReportDTO> page = complianceReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compliance-reports/:id} : get the "id" complianceReport.
     *
     * @param id the id of the complianceReportDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the complianceReportDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComplianceReportDTO> getComplianceReport(@PathVariable("id") Long id) {
        log.debug("REST request to get ComplianceReport : {}", id);
        Optional<ComplianceReportDTO> complianceReportDTO = complianceReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(complianceReportDTO);
    }

    /**
     * {@code DELETE  /compliance-reports/:id} : delete the "id" complianceReport.
     *
     * @param id the id of the complianceReportDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplianceReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete ComplianceReport : {}", id);
        complianceReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
