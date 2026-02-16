package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.AuditTrail;
import com.ecotrack.api.repository.AuditTrailRepository;
import com.ecotrack.api.service.AuditTrailService;
import com.ecotrack.api.service.dto.AuditTrailDTO;
import com.ecotrack.api.service.mapper.AuditTrailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AuditTrail}.
 */
@Service
@Transactional
public class AuditTrailServiceImpl implements AuditTrailService {

    private static final Logger log = LoggerFactory.getLogger(AuditTrailServiceImpl.class);

    private final AuditTrailRepository auditTrailRepository;

    private final AuditTrailMapper auditTrailMapper;

    public AuditTrailServiceImpl(AuditTrailRepository auditTrailRepository, AuditTrailMapper auditTrailMapper) {
        this.auditTrailRepository = auditTrailRepository;
        this.auditTrailMapper = auditTrailMapper;
    }

    @Override
    public AuditTrailDTO save(AuditTrailDTO auditTrailDTO) {
        log.debug("Request to save AuditTrail : {}", auditTrailDTO);
        AuditTrail auditTrail = auditTrailMapper.toEntity(auditTrailDTO);
        auditTrail = auditTrailRepository.save(auditTrail);
        return auditTrailMapper.toDto(auditTrail);
    }

    @Override
    public AuditTrailDTO update(AuditTrailDTO auditTrailDTO) {
        log.debug("Request to update AuditTrail : {}", auditTrailDTO);
        AuditTrail auditTrail = auditTrailMapper.toEntity(auditTrailDTO);
        auditTrail = auditTrailRepository.save(auditTrail);
        return auditTrailMapper.toDto(auditTrail);
    }

    @Override
    public Optional<AuditTrailDTO> partialUpdate(AuditTrailDTO auditTrailDTO) {
        log.debug("Request to partially update AuditTrail : {}", auditTrailDTO);

        return auditTrailRepository
            .findById(auditTrailDTO.getId())
            .map(existingAuditTrail -> {
                auditTrailMapper.partialUpdate(existingAuditTrail, auditTrailDTO);

                return existingAuditTrail;
            })
            .map(auditTrailRepository::save)
            .map(auditTrailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditTrailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AuditTrails");
        return auditTrailRepository.findAll(pageable).map(auditTrailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuditTrailDTO> findOne(Long id) {
        log.debug("Request to get AuditTrail : {}", id);
        return auditTrailRepository.findById(id).map(auditTrailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AuditTrail : {}", id);
        auditTrailRepository.deleteById(id);
    }
}
