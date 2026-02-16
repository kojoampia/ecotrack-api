package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.Installation;
import com.ecotrack.api.repository.InstallationRepository;
import com.ecotrack.api.service.InstallationService;
import com.ecotrack.api.service.dto.InstallationDTO;
import com.ecotrack.api.service.mapper.InstallationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Installation}.
 */
@Service
@Transactional
public class InstallationServiceImpl implements InstallationService {

    private final Logger log = LoggerFactory.getLogger(InstallationServiceImpl.class);

    private final InstallationRepository installationRepository;

    private final InstallationMapper installationMapper;

    public InstallationServiceImpl(InstallationRepository installationRepository, InstallationMapper installationMapper) {
        this.installationRepository = installationRepository;
        this.installationMapper = installationMapper;
    }

    @Override
    public InstallationDTO save(InstallationDTO installationDTO) {
        log.debug("Request to save Installation : {}", installationDTO);
        Installation installation = installationMapper.toEntity(installationDTO);
        installation = installationRepository.save(installation);
        return installationMapper.toDto(installation);
    }

    @Override
    public InstallationDTO update(InstallationDTO installationDTO) {
        log.debug("Request to update Installation : {}", installationDTO);
        Installation installation = installationMapper.toEntity(installationDTO);
        installation = installationRepository.save(installation);
        return installationMapper.toDto(installation);
    }

    @Override
    public Optional<InstallationDTO> partialUpdate(InstallationDTO installationDTO) {
        log.debug("Request to partially update Installation : {}", installationDTO);

        return installationRepository
            .findById(installationDTO.getId())
            .map(existingInstallation -> {
                if (installationDTO.getInstallationName() != null) {
                    existingInstallation.setInstallationName(installationDTO.getInstallationName());
                }
                if (installationDTO.getCountry() != null) {
                    existingInstallation.setCountry(installationDTO.getCountry());
                }
                if (installationDTO.getUnlocode() != null) {
                    existingInstallation.setUnlocode(installationDTO.getUnlocode());
                }
                if (installationDTO.getTenantId() != null) {
                    existingInstallation.setTenantId(installationDTO.getTenantId());
                }

                return existingInstallation;
            })
            .map(installationRepository::save)
            .map(installationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstallationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Installations");
        return installationRepository.findAll(pageable).map(installationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstallationDTO> findAllByTenant(String tenantId, Pageable pageable) {
        log.debug("Request to get all Installations for tenant: {}", tenantId);
        return installationRepository.findByTenantId(tenantId, pageable).map(installationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstallationDTO> findOne(Long id) {
        log.debug("Request to get Installation : {}", id);
        return installationRepository.findById(id).map(installationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InstallationDTO> findOneByTenant(Long id, String tenantId) {
        log.debug("Request to get Installation : {} for tenant: {}", id, tenantId);
        return installationRepository.findByIdAndTenantId(id, tenantId).map(installationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Installation : {}", id);
        installationRepository.deleteById(id);
    }

    @Override
    public void deleteByTenant(Long id, String tenantId) {
        log.debug("Request to delete Installation : {} for tenant: {}", id, tenantId);
        installationRepository
            .findByIdAndTenantId(id, tenantId)
            .ifPresent(installation -> installationRepository.deleteById(installation.getId()));
    }
}
