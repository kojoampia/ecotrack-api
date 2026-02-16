package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Importer;
import com.ecotrack.api.service.dto.ImporterDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Importer} and its DTO {@link ImporterDTO}.
 */
@Service
public class ImporterMapper {

    public Importer toEntity(ImporterDTO dto) {
        if (dto == null) {
            return null;
        }

        Importer importer = new Importer();
        importer.setId(dto.getId());
        importer.setTenantId(dto.getTenantId());
        importer.setCompanyName(dto.getCompanyName());
        importer.setEoriNumber(dto.getEoriNumber());
        importer.setVatNumber(dto.getVatNumber());
        importer.setSector(dto.getSector());
        importer.setCountry(dto.getCountry());
        importer.setAddress(dto.getAddress());
        importer.setContactName(dto.getContactName());
        importer.setContactEmail(dto.getContactEmail());
        importer.setContactPhone(dto.getContactPhone());
        importer.setAuthorizedRepresentative(dto.getAuthorizedRepresentative());
        importer.setEstimatedAnnualImportVolume(dto.getEstimatedAnnualImportVolume());
        importer.setAgreedToTerms(dto.getAgreedToTerms());

        return importer;
    }

    public ImporterDTO toDto(Importer entity) {
        if (entity == null) {
            return null;
        }

        ImporterDTO dto = new ImporterDTO();
        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setEoriNumber(entity.getEoriNumber());
        dto.setVatNumber(entity.getVatNumber());
        dto.setSector(entity.getSector());
        dto.setCountry(entity.getCountry());
        dto.setAddress(entity.getAddress());
        dto.setContactName(entity.getContactName());
        dto.setContactEmail(entity.getContactEmail());
        dto.setContactPhone(entity.getContactPhone());
        dto.setAuthorizedRepresentative(entity.getAuthorizedRepresentative());
        dto.setEstimatedAnnualImportVolume(entity.getEstimatedAnnualImportVolume());
        dto.setAgreedToTerms(entity.getAgreedToTerms());

        return dto;
    }

    public void partialUpdate(Importer entity, ImporterDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getCompanyName() != null) {
            entity.setCompanyName(dto.getCompanyName());
        }
        if (dto.getEoriNumber() != null) {
            entity.setEoriNumber(dto.getEoriNumber());
        }
        if (dto.getVatNumber() != null) {
            entity.setVatNumber(dto.getVatNumber());
        }
        if (dto.getSector() != null) {
            entity.setSector(dto.getSector());
        }
        if (dto.getCountry() != null) {
            entity.setCountry(dto.getCountry());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getContactName() != null) {
            entity.setContactName(dto.getContactName());
        }
        if (dto.getContactEmail() != null) {
            entity.setContactEmail(dto.getContactEmail());
        }
        if (dto.getContactPhone() != null) {
            entity.setContactPhone(dto.getContactPhone());
        }
        if (dto.getAuthorizedRepresentative() != null) {
            entity.setAuthorizedRepresentative(dto.getAuthorizedRepresentative());
        }
        if (dto.getEstimatedAnnualImportVolume() != null) {
            entity.setEstimatedAnnualImportVolume(dto.getEstimatedAnnualImportVolume());
        }
        if (dto.getAgreedToTerms() != null) {
            entity.setAgreedToTerms(dto.getAgreedToTerms());
        }
    }
}
