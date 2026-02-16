package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.EmissionEvidence;
import com.ecotrack.api.repository.EmissionEvidenceRepository;
import com.ecotrack.api.service.EmissionEvidenceService;
import com.ecotrack.api.service.dto.EmissionEvidenceDTO;
import com.ecotrack.api.service.mapper.EmissionEvidenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmissionEvidence}.
 */
@Service
@Transactional
public class EmissionEvidenceServiceImpl implements EmissionEvidenceService {

    private static final Logger log = LoggerFactory.getLogger(EmissionEvidenceServiceImpl.class);

    private final EmissionEvidenceRepository emissionEvidenceRepository;

    private final EmissionEvidenceMapper emissionEvidenceMapper;

    public EmissionEvidenceServiceImpl(
        EmissionEvidenceRepository emissionEvidenceRepository,
        EmissionEvidenceMapper emissionEvidenceMapper
    ) {
        this.emissionEvidenceRepository = emissionEvidenceRepository;
        this.emissionEvidenceMapper = emissionEvidenceMapper;
    }

    @Override
    public EmissionEvidenceDTO save(EmissionEvidenceDTO emissionEvidenceDTO) {
        log.debug("Request to save EmissionEvidence : {}", emissionEvidenceDTO);
        EmissionEvidence emissionEvidence = emissionEvidenceMapper.toEntity(emissionEvidenceDTO);
        emissionEvidence = emissionEvidenceRepository.save(emissionEvidence);
        return emissionEvidenceMapper.toDto(emissionEvidence);
    }

    @Override
    public EmissionEvidenceDTO update(EmissionEvidenceDTO emissionEvidenceDTO) {
        log.debug("Request to update EmissionEvidence : {}", emissionEvidenceDTO);
        EmissionEvidence emissionEvidence = emissionEvidenceMapper.toEntity(emissionEvidenceDTO);
        emissionEvidence = emissionEvidenceRepository.save(emissionEvidence);
        return emissionEvidenceMapper.toDto(emissionEvidence);
    }

    @Override
    public Optional<EmissionEvidenceDTO> partialUpdate(EmissionEvidenceDTO emissionEvidenceDTO) {
        log.debug("Request to partially update EmissionEvidence : {}", emissionEvidenceDTO);

        return emissionEvidenceRepository
            .findById(emissionEvidenceDTO.getId())
            .map(existingEmissionEvidence -> {
                emissionEvidenceMapper.partialUpdate(existingEmissionEvidence, emissionEvidenceDTO);
                return existingEmissionEvidence;
            })
            .map(emissionEvidenceRepository::save)
            .map(emissionEvidenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmissionEvidenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmissionEvidence");
        return emissionEvidenceRepository.findAll(pageable).map(emissionEvidenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmissionEvidenceDTO> findOne(Long id) {
        log.debug("Request to get EmissionEvidence : {}", id);
        return emissionEvidenceRepository.findById(id).map(emissionEvidenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmissionEvidence : {}", id);
        emissionEvidenceRepository.deleteById(id);
    }
}
