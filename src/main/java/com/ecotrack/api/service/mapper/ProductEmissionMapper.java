package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.ProductEmission;
import com.ecotrack.api.service.dto.ProductEmissionDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link ProductEmission} and its DTO {@link ProductEmissionDTO}.
 */
@Service
public class ProductEmissionMapper {

    public ProductEmissionDTO toDto(ProductEmission entity) {
        if (entity == null) {
            return null;
        }

        ProductEmissionDTO dto = new ProductEmissionDTO();

        dto.setId(entity.getId());
        dto.setTenantId(entity.getTenantId());
        dto.setCnCode(entity.getCnCode());
        dto.setQuantity(entity.getQuantity());
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());

        return dto;
    }

    public ProductEmission toEntity(ProductEmissionDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductEmission entity = new ProductEmission();

        entity.setId(dto.getId());
        entity.setTenantId(dto.getTenantId());
        entity.setCnCode(dto.getCnCode());
        entity.setQuantity(dto.getQuantity());
        entity.setProductId(dto.getProductId());
        entity.setProductName(dto.getProductName());

        return entity;
    }

    public void partialUpdate(ProductEmission entity, ProductEmissionDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getCnCode() != null) {
            entity.setCnCode(dto.getCnCode());
        }
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getProductId() != null) {
            entity.setProductId(dto.getProductId());
        }
        if (dto.getProductName() != null) {
            entity.setProductName(dto.getProductName());
        }
    }
}
