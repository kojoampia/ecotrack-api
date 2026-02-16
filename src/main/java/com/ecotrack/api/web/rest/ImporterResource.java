package com.ecotrack.api.web.rest;

import com.ecotrack.api.repository.ImporterRepository;
import com.ecotrack.api.service.ImporterService;
import com.ecotrack.api.service.dto.ImporterDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.ecotrack.api.domain.Importer}.
 */
@RestController
@RequestMapping("/api/importers")
public class ImporterResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImporterResource.class);

    private final ImporterService importerService;

    private final ImporterRepository importerRepository;

    public ImporterResource(ImporterService importerService, ImporterRepository importerRepository) {
        this.importerService = importerService;
        this.importerRepository = importerRepository;
    }

    /**
     * {@code POST  /api/importers} : Create a new importer.
     *
     * @param importerDTO the importerDTO to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new importerDTO, or with status {@code 400 (Bad Request)} if the importer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("")
    public ResponseEntity<ImporterDTO> createImporter(@Valid @RequestBody ImporterDTO importerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Importer : {}", importerDTO);
        if (importerDTO.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        ImporterDTO result = importerService.save(importerDTO);
        return ResponseEntity.created(new URI("/api/importers/" + result.getId())).body(result);
    }

    /**
     * {@code PUT  /api/importers/:id} : Updates an existing importer.
     *
     * @param id the id of the importerDTO to save
     * @param importerDTO the importerDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importerDTO,
     * or with status {@code 400 (Bad Request)} if the importerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the importerDTO is not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImporterDTO> updateImporter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImporterDTO importerDTO
    ) {
        LOG.debug("REST request to update Importer : {}, {}", id, importerDTO);
        if (importerDTO.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(id, importerDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!importerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ImporterDTO result = importerService.update(importerDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /api/importers/:id} : Partial updates given fields of an existing importer.
     *
     * @param id the id of the importerDTO to save
     * @param importerDTO the importerDTO to update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importerDTO,
     * or with status {@code 400 (Bad Request)} if the importerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the importerDTO is not found
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImporterDTO> partialUpdateImporter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImporterDTO importerDTO
    ) {
        LOG.debug("REST request to partial update Importer partially : {}, {}", id, importerDTO);
        if (importerDTO.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (!Objects.equals(id, importerDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!importerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Optional<ImporterDTO> result = importerService.partialUpdate(importerDTO);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /api/importers} : get all the importers.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importers in body
     */
    @GetMapping("")
    public ResponseEntity<List<ImporterDTO>> getAllImporters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Importers");
        Page<ImporterDTO> page = importerService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /api/importers/country/:country} : get all importers by country.
     *
     * @param country the country code
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importers in body
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<ImporterDTO>> getImportersByCountry(@PathVariable String country) {
        LOG.debug("REST request to get Importers by country : {}", country);
        List<ImporterDTO> result = importerService.findByCountry(country);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /api/importers/:id} : get the "id" importer.
     *
     * @param id the id of the importerDTO to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the importerDTO, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImporterDTO> getImporter(@PathVariable Long id) {
        LOG.debug("REST request to get Importer : {}", id);
        Optional<ImporterDTO> importerDTO = importerService.findOne(id);
        return importerDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /api/importers/:id} : delete the "id" importer.
     *
     * @param id the id of the importerDTO to delete
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImporter(@PathVariable Long id) {
        LOG.debug("REST request to delete Importer : {}", id);
        importerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
