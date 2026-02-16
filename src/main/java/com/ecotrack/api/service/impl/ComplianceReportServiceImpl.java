package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.ComplianceReport;
import com.ecotrack.api.repository.ComplianceReportRepository;
import com.ecotrack.api.service.ComplianceReportService;
import com.ecotrack.api.service.dto.ComplianceReportDTO;
import com.ecotrack.api.service.mapper.ComplianceReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ComplianceReport}.
 */
@Service
@Transactional
public class ComplianceReportServiceImpl implements ComplianceReportService {

    private static final Logger log = LoggerFactory.getLogger(ComplianceReportServiceImpl.class);

    private final ComplianceReportRepository complianceReportRepository;

    private final ComplianceReportMapper complianceReportMapper;

    public ComplianceReportServiceImpl(
        ComplianceReportRepository complianceReportRepository,
        ComplianceReportMapper complianceReportMapper
    ) {
        this.complianceReportRepository = complianceReportRepository;
        this.complianceReportMapper = complianceReportMapper;
    }

    @Override
    public ComplianceReportDTO save(ComplianceReportDTO complianceReportDTO) {
        log.debug("Request to save ComplianceReport : {}", complianceReportDTO);
        ComplianceReport complianceReport = complianceReportMapper.toEntity(complianceReportDTO);
        complianceReport = complianceReportRepository.save(complianceReport);
        return complianceReportMapper.toDto(complianceReport);
    }

    @Override
    public ComplianceReportDTO update(ComplianceReportDTO complianceReportDTO) {
        log.debug("Request to update ComplianceReport : {}", complianceReportDTO);
        ComplianceReport complianceReport = complianceReportMapper.toEntity(complianceReportDTO);
        complianceReport = complianceReportRepository.save(complianceReport);
        return complianceReportMapper.toDto(complianceReport);
    }

    @Override
    public Optional<ComplianceReportDTO> partialUpdate(ComplianceReportDTO complianceReportDTO) {
        log.debug("Request to partially update ComplianceReport : {}", complianceReportDTO);

        return complianceReportRepository
            .findById(complianceReportDTO.getId())
            .map(existingComplianceReport -> {
                complianceReportMapper.partialUpdate(existingComplianceReport, complianceReportDTO);

                return existingComplianceReport;
            })
            .map(complianceReportRepository::save)
            .map(complianceReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplianceReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ComplianceReports");
        return complianceReportRepository.findAll(pageable).map(complianceReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComplianceReportDTO> findOne(Long id) {
        log.debug("Request to get ComplianceReport : {}", id);
        return complianceReportRepository.findById(id).map(complianceReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ComplianceReport : {}", id);
        complianceReportRepository.deleteById(id);
    }
}
