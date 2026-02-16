package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.AiBudget;
import com.ecotrack.api.repository.AiBudgetRepository;
import com.ecotrack.api.service.AiBudgetService;
import com.ecotrack.api.service.dto.AiBudgetDTO;
import com.ecotrack.api.service.mapper.AiBudgetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AiBudget}.
 */
@Service
@Transactional
public class AiBudgetServiceImpl implements AiBudgetService {

    private static final Logger log = LoggerFactory.getLogger(AiBudgetServiceImpl.class);

    private final AiBudgetRepository aiBudgetRepository;
    private final AiBudgetMapper aiBudgetMapper;

    public AiBudgetServiceImpl(AiBudgetRepository aiBudgetRepository, AiBudgetMapper aiBudgetMapper) {
        this.aiBudgetRepository = aiBudgetRepository;
        this.aiBudgetMapper = aiBudgetMapper;
    }

    @Override
    public AiBudgetDTO save(AiBudgetDTO aiBudgetDTO) {
        log.debug("Request to save AiBudget : {}", aiBudgetDTO);
        AiBudget aiBudget = aiBudgetMapper.toEntity(aiBudgetDTO);
        aiBudget = aiBudgetRepository.save(aiBudget);
        return aiBudgetMapper.toDto(aiBudget);
    }

    @Override
    public AiBudgetDTO update(AiBudgetDTO aiBudgetDTO) {
        log.debug("Request to update AiBudget : {}", aiBudgetDTO);
        AiBudget aiBudget = aiBudgetMapper.toEntity(aiBudgetDTO);
        aiBudget = aiBudgetRepository.save(aiBudget);
        return aiBudgetMapper.toDto(aiBudget);
    }

    @Override
    public Optional<AiBudgetDTO> partialUpdate(AiBudgetDTO aiBudgetDTO) {
        log.debug("Request to partially update AiBudget : {}", aiBudgetDTO);

        return aiBudgetRepository
            .findById(aiBudgetDTO.getId())
            .map(existingAiBudget -> {
                aiBudgetMapper.partialUpdate(existingAiBudget, aiBudgetDTO);
                return existingAiBudget;
            })
            .map(aiBudgetRepository::save)
            .map(aiBudgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AiBudgetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AiBudgets");
        return aiBudgetRepository.findAll(pageable).map(aiBudgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AiBudgetDTO> findByTenantId(String tenantId, Pageable pageable) {
        log.debug("Request to get AiBudgets for tenant: {}", tenantId);
        return aiBudgetRepository.findByTenantId(tenantId, pageable).map(aiBudgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiBudgetDTO> findOne(Long id) {
        log.debug("Request to get AiBudget : {}", id);
        return aiBudgetRepository.findById(id).map(aiBudgetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AiBudget : {}", id);
        aiBudgetRepository.deleteById(id);
    }
}
