package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.SupplierType;
import com.ecotrack.api.repository.SupplierTypeRepository;
import com.ecotrack.api.service.SupplierTypeService;
import com.ecotrack.api.service.dto.SupplierTypeDTO;
import com.ecotrack.api.service.mapper.SupplierTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SupplierType}.
 */
@Service
@Transactional
public class SupplierTypeServiceImpl implements SupplierTypeService {

    private static final Logger log = LoggerFactory.getLogger(SupplierTypeServiceImpl.class);

    private final SupplierTypeRepository supplierTypeRepository;

    private final SupplierTypeMapper supplierTypeMapper;

    public SupplierTypeServiceImpl(SupplierTypeRepository supplierTypeRepository, SupplierTypeMapper supplierTypeMapper) {
        this.supplierTypeRepository = supplierTypeRepository;
        this.supplierTypeMapper = supplierTypeMapper;
    }

    @Override
    public SupplierTypeDTO save(SupplierTypeDTO supplierTypeDTO) {
        log.debug("Request to save SupplierType : {}", supplierTypeDTO);
        SupplierType supplierType = supplierTypeMapper.toEntity(supplierTypeDTO);
        supplierType = supplierTypeRepository.save(supplierType);
        return supplierTypeMapper.toDto(supplierType);
    }

    @Override
    public SupplierTypeDTO update(SupplierTypeDTO supplierTypeDTO) {
        log.debug("Request to save SupplierType : {}", supplierTypeDTO);
        SupplierType supplierType = supplierTypeMapper.toEntity(supplierTypeDTO);
        supplierType = supplierTypeRepository.save(supplierType);
        return supplierTypeMapper.toDto(supplierType);
    }

    @Override
    public Optional<SupplierTypeDTO> partialUpdate(SupplierTypeDTO supplierTypeDTO) {
        log.debug("Request to partially update SupplierType : {}", supplierTypeDTO);

        return supplierTypeRepository
            .findById(supplierTypeDTO.getId())
            .map(existingSupplierType -> {
                supplierTypeMapper.partialUpdate(existingSupplierType, supplierTypeDTO);

                return existingSupplierType;
            })
            .map(supplierTypeRepository::save)
            .map(supplierTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierTypes");
        return supplierTypeRepository.findAll(pageable).map(supplierTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierTypeDTO> findByTenantId(String tenantId, Pageable pageable) {
        log.debug("Request to get SupplierTypes for tenant: {}", tenantId);
        return supplierTypeRepository.findAll(pageable).map(supplierTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierTypeDTO> findOne(Long id) {
        log.debug("Request to get SupplierType : {}", id);
        return supplierTypeRepository.findById(id).map(supplierTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SupplierType : {}", id);
        supplierTypeRepository.deleteById(id);
    }
}
