package com.ecotrack.api.web.rest;

import com.ecotrack.api.service.CarbonCalculationService;
import com.ecotrack.api.service.dto.CarbonCalculationRequestDTO;
import com.ecotrack.api.service.dto.CarbonCalculationResultDTO;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import com.ecotrack.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for strategy-based carbon calculations.
 */
@RestController
@RequestMapping("/api/carbon-calculations")
public class CarbonCalculationResource {

    private static final Logger log = LoggerFactory.getLogger(CarbonCalculationResource.class);
    private static final String ENTITY_NAME = "carbonCalculation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarbonCalculationService carbonCalculationService;

    public CarbonCalculationResource(CarbonCalculationService carbonCalculationService) {
        this.carbonCalculationService = carbonCalculationService;
    }

    /**
     * {@code POST /carbon-calculations/estimate} : Calculate emissions without persistence.
     *
     * @param request the carbon calculation input
     * @return calculated emissions and inputs used
     */
    @PostMapping("/estimate")
    public ResponseEntity<CarbonCalculationResultDTO> estimate(@Valid @RequestBody CarbonCalculationRequestDTO request) {
        log.debug("REST request to estimate carbon emissions: {}", request);
        try {
            CarbonCalculationResultDTO result = carbonCalculationService.calculate(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "invalidcalculationinput");
        }
    }

    /**
     * {@code POST /carbon-calculations} : Calculate emissions and persist an emission record.
     *
     * @param request the carbon calculation input
     * @return persisted emission record
     * @throws URISyntaxException if generated location URI is invalid
     */
    @PostMapping("")
    public ResponseEntity<EmissionRecordDTO> calculateAndPersist(@Valid @RequestBody CarbonCalculationRequestDTO request)
        throws URISyntaxException {
        log.debug("REST request to calculate and persist carbon emissions: {}", request);
        try {
            EmissionRecordDTO result = carbonCalculationService.calculateAndSaveEmissionRecord(request);
            return ResponseEntity.created(new URI("/api/emission-records/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "invalidcalculationinput");
        }
    }

    /**
     * {@code POST /carbon-calculations/emission-report} : Calculate and map results into an emission report.
     *
     * @param request the carbon calculation input
     * @return persisted emission report mapped from calculation
     * @throws URISyntaxException if generated location URI is invalid
     */
    @PostMapping("/emission-report")
    public ResponseEntity<EmissionReportDTO> calculateToEmissionReport(@Valid @RequestBody CarbonCalculationRequestDTO request)
        throws URISyntaxException {
        log.debug("REST request to calculate carbon emissions and map to emission report: {}", request);
        try {
            EmissionReportDTO result = carbonCalculationService.calculateToEmissionReport(request);
            return ResponseEntity.created(new URI("/api/emission-reports/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException(ex.getMessage(), ENTITY_NAME, "invalidcalculationinput");
        }
    }
}
