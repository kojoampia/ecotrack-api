package com.ecotrack.api.service.impl;

import com.ecotrack.api.domain.Importer;
import com.ecotrack.api.repository.ImporterRepository;
import com.ecotrack.api.service.ImporterService;
import com.ecotrack.api.service.dto.ImporterDTO;
import com.ecotrack.api.service.mapper.ImporterMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Importer}.
 */
@Service
@Transactional
public class ImporterServiceImpl implements ImporterService {

    private static final Logger LOG = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private final ImporterRepository importerRepository;

    private final ImporterMapper importerMapper;

    public ImporterServiceImpl(ImporterRepository importerRepository, ImporterMapper importerMapper) {
        this.importerRepository = importerRepository;
        this.importerMapper = importerMapper;
    }

    @Override
    public ImporterDTO save(ImporterDTO importerDTO) {
        LOG.debug("Request to save Importer : {}", importerDTO);
        Importer importer = importerMapper.toEntity(importerDTO);
        importer = importerRepository.save(importer);
        return importerMapper.toDto(importer);
    }

    @Override
    public ImporterDTO update(ImporterDTO importerDTO) {
        LOG.debug("Request to update Importer : {}", importerDTO);
        Importer importer = importerMapper.toEntity(importerDTO);
        importer = importerRepository.save(importer);
        return importerMapper.toDto(importer);
    }

    @Override
    public Optional<ImporterDTO> partialUpdate(ImporterDTO importerDTO) {
        LOG.debug("Request to partially update Importer : {}", importerDTO);

        return importerRepository
            .findById(importerDTO.getId())
            .map(existingImporter -> {
                importerMapper.partialUpdate(existingImporter, importerDTO);
                return existingImporter;
            })
            .map(importerRepository::save)
            .map(importerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImporterDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Importers");
        return importerRepository.findAll(pageable).map(importerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImporterDTO> findByTenantId(String tenantId, Pageable pageable) {
        LOG.debug("Request to get Importers for tenant: {}", tenantId);
        List<Importer> importers = importerRepository.findByTenantId(tenantId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), importers.size());
        List<ImporterDTO> pageContent = importers.subList(start, end).stream().map(importerMapper::toDto).toList();
        return new PageImpl<>(pageContent, pageable, importers.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImporterDTO> findByCountry(String country) {
        LOG.debug("Request to get Importers by country: {}", country);
        return importerRepository.findByCountry(country).stream().map(importerMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImporterDTO> findOne(Long id) {
        LOG.debug("Request to get Importer : {}", id);
        return importerRepository.findById(id).map(importerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImporterDTO> findByEoriNumber(String eoriNumber) {
        LOG.debug("Request to get Importer by EORI number : {}", eoriNumber);
        return importerRepository.findByEoriNumber(eoriNumber).map(importerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Importer : {}", id);
        importerRepository.deleteById(id);
    }
}
