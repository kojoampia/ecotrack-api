package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.PriceTierRepository;
import com.ecotrack.api.service.PriceTierService;
import com.ecotrack.api.service.dto.PriceTierDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.PriceTier}.
 */
@RestController
@RequestMapping("/api/price-tiers")
public class PriceTierResource {

    private static final Logger log = LoggerFactory.getLogger(PriceTierResource.class);

    private static final String ENTITY_NAME = "priceTier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceTierService priceTierService;
    private final PriceTierRepository priceTierRepository;

    public PriceTierResource(PriceTierService priceTierService, PriceTierRepository priceTierRepository) {
        this.priceTierService = priceTierService;
        this.priceTierRepository = priceTierRepository;
    }

    /**
     * {@code POST  /price-tiers} : Create a new priceTier.
     */
    @PostMapping("")
    public ResponseEntity<PriceTierDTO> createPriceTier(@Valid @RequestBody PriceTierDTO priceTierDTO) throws URISyntaxException {
        log.debug("REST request to save PriceTier : {}", priceTierDTO);
        if (priceTierDTO.getId() != null) {
            throw new BadRequestAlertException("A new priceTier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceTierDTO result = priceTierService.save(priceTierDTO);
        return ResponseEntity.created(new URI("/api/price-tiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-tiers/:id} : Updates an existing priceTier.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PriceTierDTO> updatePriceTier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceTierDTO priceTierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PriceTier : {}, {}", id, priceTierDTO);
        if (priceTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceTierDTO result = priceTierService.update(priceTierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceTierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-tiers/:id} : Partial updates given fields of an existing priceTier, field will ignore if it is null.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceTierDTO> partialUpdatePriceTier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceTierDTO priceTierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriceTier partially : {}, {}", id, priceTierDTO);
        if (priceTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceTierDTO> result = priceTierService.partialUpdate(priceTierDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /price-tiers} : get all the priceTiers.
     */
    @GetMapping("")
    public ResponseEntity<List<PriceTierDTO>> getAllPriceTiers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PriceTiers");
        Page<PriceTierDTO> page = priceTierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-tiers/:id} : get the "id" priceTier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PriceTierDTO> getPriceTier(@PathVariable("id") Long id) {
        log.debug("REST request to get PriceTier : {}", id);
        Optional<PriceTierDTO> priceTierDTO = priceTierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceTierDTO);
    }

    /**
     * {@code DELETE  /price-tiers/:id} : delete the "id" priceTier.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceTier(@PathVariable("id") Long id) {
        log.debug("REST request to delete PriceTier : {}", id);
        priceTierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
