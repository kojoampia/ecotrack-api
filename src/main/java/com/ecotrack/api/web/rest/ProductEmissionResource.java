package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.ProductEmissionRepository;
import com.ecotrack.api.service.ProductEmissionService;
import com.ecotrack.api.service.dto.ProductEmissionDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.ProductEmission}.
 */
@RestController
@RequestMapping("/api/product-emissions")
public class ProductEmissionResource {

    private static final Logger log = LoggerFactory.getLogger(ProductEmissionResource.class);

    private static final String ENTITY_NAME = "productEmission";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final ProductEmissionService productEmissionService;

    private final ProductEmissionRepository productEmissionRepository;

    public ProductEmissionResource(ProductEmissionService productEmissionService, ProductEmissionRepository productEmissionRepository) {
        this.productEmissionService = productEmissionService;
        this.productEmissionRepository = productEmissionRepository;
    }

    /**
     * {@code POST  /product-emissions} : Create a new product emission.
     *
     * @param productEmissionDTO the productEmissionDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productEmissionDTO, or with status {@code 400 (Bad Request)} if the product emission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<ProductEmissionDTO> createProductEmission(@Valid @RequestBody ProductEmissionDTO productEmissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProductEmission : {}", productEmissionDTO);
        if (productEmissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new productEmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductEmissionDTO result = productEmissionService.save(productEmissionDTO);
        return ResponseEntity.created(new URI("/api/product-emissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-emissions/:id} : Updates an existing product emission.
     *
     * @param id the id of the productEmissionDTO to save
     * @param productEmissionDTO the productEmissionDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productEmissionDTO,
     * or with status {@code 400 (Bad Request)} if the productEmissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productEmissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductEmissionDTO> updateProductEmission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductEmissionDTO productEmissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductEmission : {}, {}", id, productEmissionDTO);
        if (productEmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productEmissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productEmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductEmissionDTO result = productEmissionService.update(productEmissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productEmissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-emissions/:id} : Partial updates given fields of an existing product emission, field will ignore if it is null
     *
     * @param id the id of the productEmissionDTO to save
     * @param productEmissionDTO the productEmissionDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productEmissionDTO,
     * or with status {@code 400 (Bad Request)} if the productEmissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productEmissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productEmissionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductEmissionDTO> partialUpdateProductEmission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductEmissionDTO productEmissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductEmission partially : {}, {}", id, productEmissionDTO);
        if (productEmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productEmissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productEmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductEmissionDTO> result = productEmissionService.partialUpdate(productEmissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productEmissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-emissions} : get all the product emissions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productEmissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductEmissionDTO>> getAllProductEmissions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProductEmissions");
        Page<ProductEmissionDTO> page = productEmissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-emissions/tenant/:tenantId} : get all the product emissions for a tenant.
     *
     * @param tenantId the tenant ID.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productEmissions in body.
     */
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ProductEmissionDTO>> getProductEmissionsByTenant(
        @PathVariable String tenantId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductEmissions for tenant : {}", tenantId);
        Page<ProductEmissionDTO> page = productEmissionService.findAllByTenantId(tenantId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-emissions/:id} : get the "id" product emission.
     *
     * @param id the id of the productEmissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productEmissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductEmissionDTO> getProductEmission(@PathVariable Long id) {
        log.debug("REST request to get ProductEmission : {}", id);
        Optional<ProductEmissionDTO> productEmissionDTO = productEmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productEmissionDTO);
    }

    /**
     * {@code DELETE  /product-emissions/:id} : delete the "id" product emission.
     *
     * @param id the id of the productEmissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductEmission(@PathVariable Long id) {
        log.debug("REST request to delete ProductEmission : {}", id);
        productEmissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
