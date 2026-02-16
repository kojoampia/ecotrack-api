package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.EmissionReport;
import com.ecotrack.api.repository.EmissionReportRepository;
import com.ecotrack.api.service.EmissionReportService;
import com.ecotrack.api.service.dto.EmissionReportDTO;
import com.ecotrack.api.service.mapper.EmissionReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmissionReport}.
 */
@Service
@Transactional
public class EmissionReportServiceImpl implements EmissionReportService {

    private static final Logger log = LoggerFactory.getLogger(EmissionReportServiceImpl.class);

    private final EmissionReportRepository emissionReportRepository;

    private final EmissionReportMapper emissionReportMapper;

    public EmissionReportServiceImpl(EmissionReportRepository emissionReportRepository, EmissionReportMapper emissionReportMapper) {
        this.emissionReportRepository = emissionReportRepository;
        this.emissionReportMapper = emissionReportMapper;
    }

    @Override
    public EmissionReportDTO save(EmissionReportDTO emissionReportDTO) {
        log.debug("Request to save EmissionReport : {}", emissionReportDTO);
        EmissionReport emissionReport = emissionReportMapper.toEntity(emissionReportDTO);
        emissionReport = emissionReportRepository.save(emissionReport);
        return emissionReportMapper.toDto(emissionReport);
    }

    @Override
    public EmissionReportDTO update(EmissionReportDTO emissionReportDTO) {
        log.debug("Request to update EmissionReport : {}", emissionReportDTO);
        EmissionReport emissionReport = emissionReportMapper.toEntity(emissionReportDTO);
        emissionReport = emissionReportRepository.save(emissionReport);
        return emissionReportMapper.toDto(emissionReport);
    }

    @Override
    public Optional<EmissionReportDTO> partialUpdate(EmissionReportDTO emissionReportDTO) {
        log.debug("Request to partially update EmissionReport : {}", emissionReportDTO);

        return emissionReportRepository
            .findById(emissionReportDTO.getId())
            .map(existingEmissionReport -> {
                emissionReportMapper.partialUpdate(existingEmissionReport, emissionReportDTO);

                return existingEmissionReport;
            })
            .map(emissionReportRepository::save)
            .map(emissionReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmissionReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmissionReports");
        return emissionReportRepository.findAll(pageable).map(emissionReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmissionReportDTO> findOne(Long id) {
        log.debug("Request to get EmissionReport : {}", id);
        return emissionReportRepository.findById(id).map(emissionReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmissionReport : {}", id);
        emissionReportRepository.deleteById(id);
    }
}
