package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.AiBudgetRepository;
import com.ecotrack.api.service.AiBudgetService;
import com.ecotrack.api.service.dto.AiBudgetDTO;
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
 * REST controller for managing {@link com.ecotrack.api.domain.AiBudget}.
 */
@RestController
@RequestMapping("/api/ai-budgets")
public class AiBudgetResource {

    private static final Logger log = LoggerFactory.getLogger(AiBudgetResource.class);

    private static final String ENTITY_NAME = "aiBudget";

    @Value("${ecopster.clientApp.name}")
    private String applicationName;

    private final AiBudgetService aiBudgetService;
    private final AiBudgetRepository aiBudgetRepository;

    public AiBudgetResource(AiBudgetService aiBudgetService, AiBudgetRepository aiBudgetRepository) {
        this.aiBudgetService = aiBudgetService;
        this.aiBudgetRepository = aiBudgetRepository;
    }

    /**
     * {@code POST  /ai-budgets} : Create a new aiBudget.
     *
     * @param aiBudgetDTO the aiBudgetDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aiBudgetDTO,
     * or with status {@code 400 (Bad Request)} if the aiBudget has already an ID
     */
    @PostMapping("")
    public ResponseEntity<AiBudgetDTO> createAiBudget(@Valid @RequestBody AiBudgetDTO aiBudgetDTO) throws URISyntaxException {
        log.debug("REST request to save AiBudget : {}", aiBudgetDTO);
        if (aiBudgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new aiBudget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AiBudgetDTO result = aiBudgetService.save(aiBudgetDTO);
        return ResponseEntity.created(new URI("/api/ai-budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ai-budgets/:id} : Updates an existing aiBudget.
     *
     * @param id the id of the aiBudgetDTO to save
     * @param aiBudgetDTO the aiBudgetDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiBudgetDTO,
     * or with status {@code 400 (Bad Request)} if the aiBudgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aiBudgetDTO couldn't be updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<AiBudgetDTO> updateAiBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AiBudgetDTO aiBudgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AiBudget : {}, {}", id, aiBudgetDTO);
        if (aiBudgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiBudgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiBudgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AiBudgetDTO result = aiBudgetService.update(aiBudgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aiBudgetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ai-budgets/:id} : Partial updates given fields of an existing aiBudget, field will ignore if it is null
     *
     * @param id the id of the aiBudgetDTO to save
     * @param aiBudgetDTO the aiBudgetDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiBudgetDTO,
     * or with status {@code 400 (Bad Request)} if the aiBudgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aiBudgetDTO couldn't be updated
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AiBudgetDTO> partialUpdateAiBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AiBudgetDTO aiBudgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AiBudget partially : {}, {}", id, aiBudgetDTO);
        if (aiBudgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiBudgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiBudgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AiBudgetDTO> result = aiBudgetService.partialUpdate(aiBudgetDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()));
    }

    /**
     * {@code GET  /ai-budgets} : get all the aiBudgets.
     *
     * @param pageable the pagination information
     * @param tenantId optional tenant filter
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aiBudgets in body
     */
    @GetMapping("")
    public ResponseEntity<List<AiBudgetDTO>> getAllAiBudgets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String tenantId
    ) {
        log.debug("REST request to get a page of AiBudgets");
        Page<AiBudgetDTO> page = tenantId == null ? aiBudgetService.findAll(pageable) : aiBudgetService.findByTenantId(tenantId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-budgets/:id} : get the "id" aiBudget.
     *
     * @param id the id of the aiBudgetDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aiBudgetDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AiBudgetDTO> getAiBudget(@PathVariable("id") Long id) {
        log.debug("REST request to get AiBudget : {}", id);
        Optional<AiBudgetDTO> aiBudgetDTO = aiBudgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aiBudgetDTO);
    }

    /**
     * {@code DELETE  /ai-budgets/:id} : delete the "id" aiBudget.
     *
     * @param id the id of the aiBudgetDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAiBudget(@PathVariable("id") Long id) {
        log.debug("REST request to delete AiBudget : {}", id);
        aiBudgetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
