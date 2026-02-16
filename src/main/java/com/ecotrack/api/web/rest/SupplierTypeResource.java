package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.SupplierTypeRepository;
import com.ecotrack.api.service.SupplierTypeService;
import com.ecotrack.api.service.dto.SupplierTypeDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.SupplierType}.
 */
@RestController
@RequestMapping("/api/supplier-type")
public class SupplierTypeResource {

    private static final Logger log = LoggerFactory.getLogger(SupplierTypeResource.class);

    private static final String ENTITY_NAME = "supplierType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierTypeService supplierTypeService;

    private final SupplierTypeRepository supplierTypeRepository;

    public SupplierTypeResource(SupplierTypeService supplierTypeService, SupplierTypeRepository supplierTypeRepository) {
        this.supplierTypeService = supplierTypeService;
        this.supplierTypeRepository = supplierTypeRepository;
    }

    /**
     * {@code POST  /supplier-type} : Create a new supplierType.
     *
     * @param supplierTypeDTO the supplierTypeDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierTypeDTO, or with status {@code 400 (Bad Request)} if the supplierType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<SupplierTypeDTO> createSupplierType(@Valid @RequestBody SupplierTypeDTO supplierTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save SupplierType : {}", supplierTypeDTO);
        if (supplierTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SupplierTypeDTO result = supplierTypeService.save(supplierTypeDTO);
        return ResponseEntity.created(new URI("/api/supplier-type/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /supplier-type/:id} : Updates an existing supplierType.
     *
     * @param id the id of the supplierTypeDTO to save
     * @param supplierTypeDTO the supplierTypeDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the supplierTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierTypeDTO> updateSupplierType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierTypeDTO supplierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierType : {}, {}", id, supplierTypeDTO);
        if (supplierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SupplierTypeDTO result = supplierTypeService.update(supplierTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, supplierTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /supplier-type/:id} : Partial updates given fields of an existing supplierType, field will ignore if it is null
     *
     * @param id the id of the supplierTypeDTO to save
     * @param supplierTypeDTO the supplierTypeDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the supplierTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierTypeDTO> partialUpdateSupplierType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupplierTypeDTO supplierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SupplierType partially : {}, {}", id, supplierTypeDTO);
        if (supplierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierTypeDTO> result = supplierTypeService.partialUpdate(supplierTypeDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /supplier-type} : get all the supplierTypes.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierTypes in body
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierTypeDTO>> getAllSupplierTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SupplierTypes");
        Page<SupplierTypeDTO> page = supplierTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-type/:id} : get the "id" supplierType.
     *
     * @param id the id of the supplierTypeDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierTypeDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierTypeDTO> getSupplierType(@PathVariable("id") Long id) {
        log.debug("REST request to get SupplierType : {}", id);
        Optional<SupplierTypeDTO> supplierTypeDTO = supplierTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierTypeDTO);
    }

    /**
     * {@code DELETE  /supplier-type/:id} : delete the "id" supplierType.
     *
     * @param id the id of the supplierTypeDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierType(@PathVariable("id") Long id) {
        log.debug("REST request to delete SupplierType : {}", id);
        supplierTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
