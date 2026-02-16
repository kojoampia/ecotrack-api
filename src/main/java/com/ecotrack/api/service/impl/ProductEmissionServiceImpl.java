package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.ProductEmission;
import com.ecotrack.api.repository.ProductEmissionRepository;
import com.ecotrack.api.service.ProductEmissionService;
import com.ecotrack.api.service.dto.ProductEmissionDTO;
import com.ecotrack.api.service.mapper.ProductEmissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ecotrack.api.domain.ProductEmission}.
 */
@Service
@Transactional
public class ProductEmissionServiceImpl implements ProductEmissionService {

    private final Logger log = LoggerFactory.getLogger(ProductEmissionServiceImpl.class);

    private final ProductEmissionRepository productEmissionRepository;

    private final ProductEmissionMapper productEmissionMapper;

    public ProductEmissionServiceImpl(ProductEmissionRepository productEmissionRepository, ProductEmissionMapper productEmissionMapper) {
        this.productEmissionRepository = productEmissionRepository;
        this.productEmissionMapper = productEmissionMapper;
    }

    @Override
    public ProductEmissionDTO save(ProductEmissionDTO productEmissionDTO) {
        log.debug("Request to save ProductEmission : {}", productEmissionDTO);
        ProductEmission productEmission = productEmissionMapper.toEntity(productEmissionDTO);
        productEmission = productEmissionRepository.save(productEmission);
        return productEmissionMapper.toDto(productEmission);
    }

    @Override
    public ProductEmissionDTO update(ProductEmissionDTO productEmissionDTO) {
        log.debug("Request to update ProductEmission : {}", productEmissionDTO);
        ProductEmission productEmission = productEmissionMapper.toEntity(productEmissionDTO);
        productEmission = productEmissionRepository.save(productEmission);
        return productEmissionMapper.toDto(productEmission);
    }

    @Override
    public Optional<ProductEmissionDTO> partialUpdate(ProductEmissionDTO productEmissionDTO) {
        log.debug("Request to partially update ProductEmission : {}", productEmissionDTO);

        return productEmissionRepository
            .findById(productEmissionDTO.getId())
            .map(existingProductEmission -> {
                productEmissionMapper.partialUpdate(existingProductEmission, productEmissionDTO);

                return existingProductEmission;
            })
            .map(productEmissionRepository::save)
            .map(productEmissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductEmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductEmissions");
        return productEmissionRepository.findAll(pageable).map(productEmissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductEmissionDTO> findAllByTenantId(String tenantId, Pageable pageable) {
        log.debug("Request to get all ProductEmissions for tenant: {}", tenantId);
        return productEmissionRepository.findAllByTenantId(tenantId, pageable).map(productEmissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductEmissionDTO> findOne(Long id) {
        log.debug("Request to get ProductEmission : {}", id);
        return productEmissionRepository.findById(id).map(productEmissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductEmission : {}", id);
        productEmissionRepository.deleteById(id);
    }
}
