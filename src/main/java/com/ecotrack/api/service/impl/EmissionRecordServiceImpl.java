package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.EmissionRecord;
import com.ecotrack.api.repository.EmissionRecordRepository;
import com.ecotrack.api.service.EmissionRecordService;
import com.ecotrack.api.service.dto.EmissionRecordDTO;
import com.ecotrack.api.service.mapper.EmissionRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmissionRecord}.
 */
@Service
@Transactional
public class EmissionRecordServiceImpl implements EmissionRecordService {

    private static final Logger log = LoggerFactory.getLogger(EmissionRecordServiceImpl.class);

    private final EmissionRecordRepository emissionRecordRepository;

    private final EmissionRecordMapper emissionRecordMapper;

    public EmissionRecordServiceImpl(EmissionRecordRepository emissionRecordRepository, EmissionRecordMapper emissionRecordMapper) {
        this.emissionRecordRepository = emissionRecordRepository;
        this.emissionRecordMapper = emissionRecordMapper;
    }

    @Override
    public EmissionRecordDTO save(EmissionRecordDTO emissionRecordDTO) {
        log.debug("Request to save EmissionRecord : {}", emissionRecordDTO);
        EmissionRecord emissionRecord = emissionRecordMapper.toEntity(emissionRecordDTO);
        emissionRecord = emissionRecordRepository.save(emissionRecord);
        return emissionRecordMapper.toDto(emissionRecord);
    }

    @Override
    public EmissionRecordDTO update(EmissionRecordDTO emissionRecordDTO) {
        log.debug("Request to update EmissionRecord : {}", emissionRecordDTO);
        EmissionRecord emissionRecord = emissionRecordMapper.toEntity(emissionRecordDTO);
        emissionRecord = emissionRecordRepository.save(emissionRecord);
        return emissionRecordMapper.toDto(emissionRecord);
    }

    @Override
    public Optional<EmissionRecordDTO> partialUpdate(EmissionRecordDTO emissionRecordDTO) {
        log.debug("Request to partially update EmissionRecord : {}", emissionRecordDTO);

        return emissionRecordRepository
            .findById(emissionRecordDTO.getId())
            .map(existingEmissionRecord -> {
                if (emissionRecordDTO.getTenantId() != null) {
                    existingEmissionRecord.setTenantId(emissionRecordDTO.getTenantId());
                }
                if (emissionRecordDTO.getSupplierId() != null) {
                    existingEmissionRecord.setSupplierId(emissionRecordDTO.getSupplierId());
                }
                if (emissionRecordDTO.getInstallationId() != null) {
                    existingEmissionRecord.setInstallationId(emissionRecordDTO.getInstallationId());
                }
                if (emissionRecordDTO.getProductEmissionId() != null) {
                    existingEmissionRecord.setProductEmissionId(emissionRecordDTO.getProductEmissionId());
                }
                if (emissionRecordDTO.getScope() != null) {
                    existingEmissionRecord.setScope(emissionRecordDTO.getScope());
                }
                if (emissionRecordDTO.getCarbonGrams() != null) {
                    existingEmissionRecord.setCarbonGrams(emissionRecordDTO.getCarbonGrams());
                }
                if (emissionRecordDTO.getDateRecorded() != null) {
                    existingEmissionRecord.setDateRecorded(emissionRecordDTO.getDateRecorded());
                }
                if (emissionRecordDTO.getSource() != null) {
                    existingEmissionRecord.setSource(emissionRecordDTO.getSource());
                }
                if (emissionRecordDTO.getNotes() != null) {
                    existingEmissionRecord.setNotes(emissionRecordDTO.getNotes());
                }
                if (emissionRecordDTO.getVerified() != null) {
                    existingEmissionRecord.setVerified(emissionRecordDTO.getVerified());
                }
                if (emissionRecordDTO.getConfidenceScore() != null) {
                    existingEmissionRecord.setConfidenceScore(emissionRecordDTO.getConfidenceScore());
                }
                if (emissionRecordDTO.getCalculationMethod() != null) {
                    existingEmissionRecord.setCalculationMethod(emissionRecordDTO.getCalculationMethod());
                }
                if (emissionRecordDTO.getUncertaintyFactor() != null) {
                    existingEmissionRecord.setUncertaintyFactor(emissionRecordDTO.getUncertaintyFactor());
                }
                if (emissionRecordDTO.getMetadata() != null) {
                    existingEmissionRecord.setMetadata(emissionRecordDTO.getMetadata());
                }

                return existingEmissionRecord;
            })
            .map(emissionRecordRepository::save)
            .map(emissionRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmissionRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmissionRecords");
        return emissionRecordRepository.findAll(pageable).map(emissionRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmissionRecordDTO> findByTenantId(String tenantId, Pageable pageable) {
        log.debug("Request to get EmissionRecords for tenant: {}", tenantId);
        return emissionRecordRepository.findByTenantId(tenantId, pageable).map(emissionRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmissionRecordDTO> findOne(Long id) {
        log.debug("Request to get EmissionRecord : {}", id);
        return emissionRecordRepository.findById(id).map(emissionRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmissionRecord : {}", id);
        emissionRecordRepository.deleteById(id);
    }
}
