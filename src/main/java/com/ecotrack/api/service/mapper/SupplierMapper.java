package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Supplier;
import com.ecotrack.api.domain.SupplierType;
import com.ecotrack.api.service.dto.SupplierDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Supplier} and its DTO called {@link SupplierDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class SupplierMapper {

    public Supplier toEntity(SupplierDTO dto) {
        if (dto == null) {
            return null;
        }

        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setCompanyName(dto.getCompanyName());
        supplier.setContactEmail(dto.getContactEmail());
        supplier.setIndustry(dto.getIndustry());
        supplier.setTier(dto.getTier());
        supplier.setAddress(dto.getAddress());
        supplier.setPhone(dto.getPhone());
        supplier.setWebsite(dto.getWebsite());
        supplier.setTenantId(dto.getTenantId());

        if (dto.getSupplierTypeId() != null) {
            SupplierType supplierType = new SupplierType();
            supplierType.setId(dto.getSupplierTypeId());
            supplier.setSupplierType(supplierType);
        }

        return supplier;
    }

    public SupplierDTO toDto(Supplier entity) {
        if (entity == null) {
            return null;
        }

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(entity.getId());
        supplierDTO.setCompanyName(entity.getCompanyName());
        supplierDTO.setContactEmail(entity.getContactEmail());
        supplierDTO.setIndustry(entity.getIndustry());
        supplierDTO.setTier(entity.getTier());
        supplierDTO.setAddress(entity.getAddress());
        supplierDTO.setPhone(entity.getPhone());
        supplierDTO.setWebsite(entity.getWebsite());
        supplierDTO.setTenantId(entity.getTenantId());

        if (entity.getSupplierType() != null) {
            supplierDTO.setSupplierTypeId(entity.getSupplierType().getId());
            supplierDTO.setSupplierTypeName(entity.getSupplierType().getName());
        }

        return supplierDTO;
    }

    public void partialUpdate(Supplier entity, SupplierDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getCompanyName() != null) {
            entity.setCompanyName(dto.getCompanyName());
        }
        if (dto.getContactEmail() != null) {
            entity.setContactEmail(dto.getContactEmail());
        }
        if (dto.getIndustry() != null) {
            entity.setIndustry(dto.getIndustry());
        }
        if (dto.getTier() != null) {
            entity.setTier(dto.getTier());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getWebsite() != null) {
            entity.setWebsite(dto.getWebsite());
        }
        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getSupplierTypeId() != null) {
            SupplierType supplierType = new SupplierType();
            supplierType.setId(dto.getSupplierTypeId());
            entity.setSupplierType(supplierType);
        }
    }

    public Set<SupplierDTO> toDtos(Set<Supplier> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
