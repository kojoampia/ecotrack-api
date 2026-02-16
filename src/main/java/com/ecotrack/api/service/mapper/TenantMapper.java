package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Tenant;
import com.ecotrack.api.service.dto.TenantDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Service
public class TenantMapper {

    public Tenant toEntity(TenantDTO dto) {
        if (dto == null) {
            return null;
        }

        Tenant tenant = new Tenant();
        tenant.setId(dto.getId());
        tenant.setName(dto.getName());
        tenant.setIndustry(dto.getIndustry());
        tenant.setRegion(dto.getRegion());
        return tenant;
    }

    public TenantDTO toDto(Tenant entity) {
        if (entity == null) {
            return null;
        }

        TenantDTO tenantDTO = new TenantDTO();
        tenantDTO.setId(entity.getId());
        tenantDTO.setName(entity.getName());
        tenantDTO.setIndustry(entity.getIndustry());
        tenantDTO.setRegion(entity.getRegion());
        return tenantDTO;
    }

    public void partialUpdate(Tenant entity, TenantDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getIndustry() != null) {
            entity.setIndustry(dto.getIndustry());
        }
        if (dto.getRegion() != null) {
            entity.setRegion(dto.getRegion());
        }
    }
}
