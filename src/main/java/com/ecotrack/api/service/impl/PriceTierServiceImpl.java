package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.PriceTier;
import com.ecotrack.api.repository.PriceTierRepository;
import com.ecotrack.api.service.PriceTierService;
import com.ecotrack.api.service.dto.PriceTierDTO;
import com.ecotrack.api.service.mapper.PriceTierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceTier}.
 */
@Service
@Transactional
public class PriceTierServiceImpl implements PriceTierService {

    private static final Logger log = LoggerFactory.getLogger(PriceTierServiceImpl.class);

    private final PriceTierRepository priceTierRepository;
    private final PriceTierMapper priceTierMapper;

    public PriceTierServiceImpl(PriceTierRepository priceTierRepository, PriceTierMapper priceTierMapper) {
        this.priceTierRepository = priceTierRepository;
        this.priceTierMapper = priceTierMapper;
    }

    @Override
    public PriceTierDTO save(PriceTierDTO priceTierDTO) {
        log.debug("Request to save PriceTier : {}", priceTierDTO);
        PriceTier priceTier = priceTierMapper.toEntity(priceTierDTO);
        priceTier = priceTierRepository.save(priceTier);
        return priceTierMapper.toDto(priceTier);
    }

    @Override
    public PriceTierDTO update(PriceTierDTO priceTierDTO) {
        log.debug("Request to update PriceTier : {}", priceTierDTO);
        PriceTier priceTier = priceTierMapper.toEntity(priceTierDTO);
        priceTier = priceTierRepository.save(priceTier);
        return priceTierMapper.toDto(priceTier);
    }

    @Override
    public Optional<PriceTierDTO> partialUpdate(PriceTierDTO priceTierDTO) {
        log.debug("Request to partially update PriceTier : {}", priceTierDTO);

        return priceTierRepository
            .findById(priceTierDTO.getId())
            .map(existingPriceTier -> {
                priceTierMapper.partialUpdate(existingPriceTier, priceTierDTO);
                return existingPriceTier;
            })
            .map(priceTierRepository::save)
            .map(priceTierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PriceTierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PriceTiers");
        return priceTierRepository.findAll(pageable).map(priceTierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceTierDTO> findOne(Long id) {
        log.debug("Request to get PriceTier : {}", id);
        return priceTierRepository.findById(id).map(priceTierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PriceTier : {}", id);
        priceTierRepository.deleteById(id);
    }
}
