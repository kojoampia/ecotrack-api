package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.Lead;
import com.ecotrack.api.repository.LeadRepository;
import com.ecotrack.api.service.LeadService;
import com.ecotrack.api.service.dto.LeadDTO;
import com.ecotrack.api.service.mapper.LeadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lead}.
 */
@Service
@Transactional
public class LeadServiceImpl implements LeadService {

    private static final Logger log = LoggerFactory.getLogger(LeadServiceImpl.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    public LeadServiceImpl(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    @Override
    public LeadDTO save(LeadDTO leadDTO) {
        log.debug("Request to save Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        return leadMapper.toDto(lead);
    }

    @Override
    public LeadDTO update(LeadDTO leadDTO) {
        log.debug("Request to update Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        return leadMapper.toDto(lead);
    }

    @Override
    public Optional<LeadDTO> partialUpdate(LeadDTO leadDTO) {
        log.debug("Request to partially update Lead : {}", leadDTO);

        return leadRepository
            .findById(leadDTO.getId())
            .map(existingLead -> {
                leadMapper.partialUpdate(existingLead, leadDTO);
                return existingLead;
            })
            .map(leadRepository::save)
            .map(leadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Leads");
        return leadRepository.findAll(pageable).map(leadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> findOne(Long id) {
        log.debug("Request to get Lead : {}", id);
        return leadRepository.findById(id).map(leadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> findByEmail(String email) {
        log.debug("Request to get Lead by email : {}", email);
        return leadRepository.findByEmail(email).map(leadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Request to check if Lead exists by email : {}", email);
        return leadRepository.existsByEmail(email);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lead : {}", id);
        leadRepository.deleteById(id);
    }
}
