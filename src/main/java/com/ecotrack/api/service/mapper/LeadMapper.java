package com.ecotrack.api.service.mapper;

import com.ecotrack.api.domain.Lead;
import com.ecotrack.api.service.dto.LeadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lead} and its DTO {@link LeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeadMapper extends EntityMapper<LeadDTO, Lead> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Lead toEntity(LeadDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void partialUpdate(@MappingTarget Lead entity, LeadDTO dto);
}
