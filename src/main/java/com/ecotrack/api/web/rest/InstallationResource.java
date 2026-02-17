package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.InstallationRepository;
import com.ecotrack.api.service.InstallationService;
import com.ecotrack.api.service.dto.InstallationDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.Installation}.
 */
@RestController
@RequestMapping("/api/installations")
public class InstallationResource {

    private final Logger log = LoggerFactory.getLogger(InstallationResource.class);

    private static final String ENTITY_NAME = "installation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstallationService installationService;

    private final InstallationRepository installationRepository;

    public InstallationResource(InstallationService installationService, InstallationRepository installationRepository) {
        this.installationService = installationService;
        this.installationRepository = installationRepository;
    }

    /**
     * {@code POST  /installations} : Create a new installation.
     *
     * @param installationDTO the installationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new installationDTO, or with status {@code 400 (Bad Request)} if the installation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InstallationDTO> createInstallation(@Valid @RequestBody InstallationDTO installationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Installation : {}", installationDTO);
        if (installationDTO.getId() != null) {
            throw new BadRequestAlertException("A new installation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstallationDTO result = installationService.save(installationDTO);
        return ResponseEntity.created(new URI("/api/installations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /installations/:id} : Updates an existing installation.
     *
     * @param id the id of the installationDTO to save.
     * @param installationDTO the installationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated installationDTO,
     * or with status {@code 400 (Bad Request)} if the installationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the installationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InstallationDTO> updateInstallation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstallationDTO installationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Installation : {}, {}", id, installationDTO);
        if (installationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, installationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!installationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstallationDTO result = installationService.update(installationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, installationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /installations/:id} : Partial updates given fields of an existing installation, field will ignore if it is null
     *
     * @param id the id of the installationDTO to save.
     * @param installationDTO the installationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated installationDTO,
     * or with status {@code 400 (Bad Request)} if the installationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the installationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstallationDTO> partialUpdateInstallation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstallationDTO installationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Installation partially : {}, {}", id, installationDTO);
        if (installationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, installationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!installationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstallationDTO> result = installationService.partialUpdate(installationDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /installations} : get all the installations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of installations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InstallationDTO>> getAllInstallations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Installations");
        Page<InstallationDTO> page = installationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /installations/tenant/:tenantId} : get all the installations for a tenant.
     *
     * @param tenantId the tenant ID.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of installations in body.
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<InstallationDTO>> getInstallationsByTenant(
        @PathVariable String tenantId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Installations for tenant: {}", tenantId);
        Page<InstallationDTO> page = installationService.findAllByTenant(tenantId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /installations/:id} : get the "id" installation.
     *
     * @param id the id of the installationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the installationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InstallationDTO> getInstallation(@PathVariable Long id) {
        log.debug("REST request to get Installation : {}", id);
        Optional<InstallationDTO> installationDTO = installationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(installationDTO);
    }

    /**
     * {@code GET  /installations/:id/tenant/:tenantId} : get the "id" installation for tenant.
     *
     * @param id the id of the installationDTO to retrieve.
     * @param tenantId the tenant ID.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the installationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}/tenant/{tenantId}")
    public ResponseEntity<InstallationDTO> getInstallationByTenant(@PathVariable Long id, @PathVariable String tenantId) {
        log.debug("REST request to get Installation : {} for tenant: {}", id, tenantId);
        Optional<InstallationDTO> installationDTO = installationService.findOneByTenant(id, tenantId);
        return ResponseUtil.wrapOrNotFound(installationDTO);
    }

    /**
     * {@code DELETE  /installations/:id} : delete the "id" installation.
     *
     * @param id the id of the installationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Long id) {
        log.debug("REST request to delete Installation : {}", id);
        installationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /installations/:id/tenant/:tenantId} : delete the "id" installation for tenant.
     *
     * @param id the id of the installationDTO to delete.
     * @param tenantId the tenant ID.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}/tenant/{tenantId}")
    public ResponseEntity<Void> deleteInstallationByTenant(@PathVariable Long id, @PathVariable String tenantId) {
        log.debug("REST request to delete Installation : {} for tenant: {}", id, tenantId);
        installationService.deleteByTenant(id, tenantId);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
