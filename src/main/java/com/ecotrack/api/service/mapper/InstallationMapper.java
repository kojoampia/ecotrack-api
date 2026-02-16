package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Installation;
import com.ecotrack.api.domain.Supplier;
import com.ecotrack.api.service.dto.InstallationDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Installation} and its DTO {@link InstallationDTO}.
 */
@Service
public class InstallationMapper {

    public Installation toEntity(InstallationDTO dto) {
        if (dto == null) {
            return null;
        }

        Installation installation = new Installation();
        installation.setId(dto.getId());
        installation.setInstallationName(dto.getInstallationName());
        installation.setCountry(dto.getCountry());
        installation.setUnlocode(dto.getUnlocode());
        installation.setTenantId(dto.getTenantId());

        if (dto.getSupplierId() != null) {
            Supplier supplier = new Supplier();
            supplier.setId(dto.getSupplierId());
            installation.setSupplier(supplier);
        }

        return installation;
    }

    public InstallationDTO toDto(Installation entity) {
        if (entity == null) {
            return null;
        }

        InstallationDTO dto = new InstallationDTO();
        dto.setId(entity.getId());
        dto.setInstallationName(entity.getInstallationName());
        dto.setCountry(entity.getCountry());
        dto.setUnlocode(entity.getUnlocode());
        dto.setTenantId(entity.getTenantId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());

        if (entity.getSupplier() != null) {
            dto.setSupplierId(entity.getSupplier().getId());
            dto.setSupplierCompanyName(entity.getSupplier().getCompanyName());
        }

        return dto;
    }
}
