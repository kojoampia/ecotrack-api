package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.Tenant;
import com.ecotrack.api.repository.TenantRepository;
import com.ecotrack.api.service.TenantService;
import com.ecotrack.api.service.dto.TenantDTO;
import com.ecotrack.api.service.mapper.TenantMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tenant}.
 */
@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private static final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Override
    public TenantDTO save(TenantDTO tenantDTO) {
        log.debug("Request to save Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    @Override
    public TenantDTO update(TenantDTO tenantDTO) {
        log.debug("Request to update Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(String id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id).map(tenantMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Tenant : {}", id);
        tenantRepository.deleteById(id);
    }
}
