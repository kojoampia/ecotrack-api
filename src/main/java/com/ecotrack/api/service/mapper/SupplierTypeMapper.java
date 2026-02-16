package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.SupplierType;
import com.ecotrack.api.service.dto.SupplierTypeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link SupplierType} and its DTO called {@link SupplierTypeDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class SupplierTypeMapper {

    public SupplierType toEntity(SupplierTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        SupplierType supplierType = new SupplierType();
        supplierType.setId(dto.getId());
        supplierType.setName(dto.getName());
        supplierType.setCategory(dto.getCategory());
        supplierType.setDescription(dto.getDescription());
        supplierType.setTenantId(dto.getTenantId());

        return supplierType;
    }

    public SupplierTypeDTO toDto(SupplierType entity) {
        if (entity == null) {
            return null;
        }

        SupplierTypeDTO supplierTypeDTO = new SupplierTypeDTO();
        supplierTypeDTO.setId(entity.getId());
        supplierTypeDTO.setName(entity.getName());
        supplierTypeDTO.setCategory(entity.getCategory());
        supplierTypeDTO.setDescription(entity.getDescription());
        supplierTypeDTO.setTenantId(entity.getTenantId());

        return supplierTypeDTO;
    }

    public void partialUpdate(SupplierType entity, SupplierTypeDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getCategory() != null) {
            entity.setCategory(dto.getCategory());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
    }

    public Set<SupplierTypeDTO> toDtos(Set<SupplierType> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
