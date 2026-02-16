package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.Supplier;
import com.ecotrack.api.repository.SupplierRepository;
import com.ecotrack.api.service.SupplierService;
import com.ecotrack.api.service.dto.SupplierDTO;
import com.ecotrack.api.service.mapper.SupplierMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Supplier}.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private static final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierDTO save(SupplierDTO supplierDTO) {
        log.debug("Request to save Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) {
        log.debug("Request to update Supplier : {}", supplierDTO);
        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(supplier);
    }

    @Override
    public Optional<SupplierDTO> partialUpdate(SupplierDTO supplierDTO) {
        log.debug("Request to partially update Supplier : {}", supplierDTO);

        return supplierRepository
            .findById(supplierDTO.getId())
            .map(existingSupplier -> {
                supplierMapper.partialUpdate(existingSupplier, supplierDTO);

                return existingSupplier;
            })
            .map(supplierRepository::save)
            .map(supplierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll(pageable).map(supplierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> findByTenantId(String tenantId, Pageable pageable) {
        log.debug("Request to get Suppliers for tenant: {}", tenantId);
        return supplierRepository.findAll(pageable).map(supplierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findByIndustry(String industry) {
        log.debug("Request to get Suppliers by industry: {}", industry);
        return supplierRepository.findByIndustry(industry).stream().map(supplierMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findByTier(Integer tier) {
        log.debug("Request to get Suppliers by tier: {}", tier);
        return supplierRepository.findByTier(tier).stream().map(supplierMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierDTO> findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id).map(supplierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
