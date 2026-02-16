package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.AiBudget;
import com.ecotrack.api.service.dto.AiBudgetDTO;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link AiBudget} and its DTO {@link AiBudgetDTO}.
 */
@Service
public class AiBudgetMapper {

    public AiBudget toEntity(AiBudgetDTO dto) {
        if (dto == null) {
            return null;
        }

        AiBudget aiBudget = new AiBudget();
        aiBudget.setId(dto.getId());
        aiBudget.setTenantId(dto.getTenantId());
        aiBudget.setAmount(dto.getAmount());
        aiBudget.setConsumed(dto.getConsumed());
        aiBudget.setLastPurchased(dto.getLastPurchased());
        aiBudget.setPurchased(dto.getPurchased());
        return aiBudget;
    }

    public AiBudgetDTO toDto(AiBudget entity) {
        if (entity == null) {
            return null;
        }

        AiBudgetDTO aiBudgetDTO = new AiBudgetDTO();
        aiBudgetDTO.setId(entity.getId());
        aiBudgetDTO.setTenantId(entity.getTenantId());
        aiBudgetDTO.setAmount(entity.getAmount());
        aiBudgetDTO.setConsumed(entity.getConsumed());
        aiBudgetDTO.setLastPurchased(entity.getLastPurchased());
        aiBudgetDTO.setPurchased(entity.getPurchased());
        return aiBudgetDTO;
    }

    public void partialUpdate(AiBudget entity, AiBudgetDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getTenantId() != null) {
            entity.setTenantId(dto.getTenantId());
        }
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getConsumed() != null) {
            entity.setConsumed(dto.getConsumed());
        }
        if (dto.getLastPurchased() != null) {
            entity.setLastPurchased(dto.getLastPurchased());
        }
        if (dto.getPurchased() != null) {
            entity.setPurchased(dto.getPurchased());
        }
    }
}
