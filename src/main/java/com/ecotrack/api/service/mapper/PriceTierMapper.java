package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.PriceTier;
import com.ecotrack.api.service.dto.PriceTierDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link PriceTier} and its DTO {@link PriceTierDTO}.
 */
@Service
public class PriceTierMapper {

    public PriceTier toEntity(PriceTierDTO dto) {
        if (dto == null) {
            return null;
        }

        PriceTier priceTier = new PriceTier();
        priceTier.setId(dto.getId());
        priceTier.setName(dto.getName());
        priceTier.setBadge(dto.getBadge());
        priceTier.setPrice(dto.getPrice());
        priceTier.setCurrency(dto.getCurrency());
        priceTier.setBillingCycle(dto.getBillingCycle());
        priceTier.setFeatures(dto.getFeatures());
        return priceTier;
    }

    public PriceTierDTO toDto(PriceTier entity) {
        if (entity == null) {
            return null;
        }

        PriceTierDTO priceTierDTO = new PriceTierDTO();
        priceTierDTO.setId(entity.getId());
        priceTierDTO.setName(entity.getName());
        priceTierDTO.setBadge(entity.getBadge());
        priceTierDTO.setPrice(entity.getPrice());
        priceTierDTO.setCurrency(entity.getCurrency());
        priceTierDTO.setBillingCycle(entity.getBillingCycle());
        priceTierDTO.setFeatures(entity.getFeatures());
        return priceTierDTO;
    }

    public void partialUpdate(PriceTier entity, PriceTierDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getBadge() != null) {
            entity.setBadge(dto.getBadge());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getBillingCycle() != null) {
            entity.setBillingCycle(dto.getBillingCycle());
        }
        if (dto.getFeatures() != null) {
            entity.setFeatures(dto.getFeatures());
        }
    }
}
