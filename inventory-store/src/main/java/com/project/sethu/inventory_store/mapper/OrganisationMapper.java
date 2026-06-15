package com.project.sethu.inventory_store.mapper;


import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.Organisation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrganisationMapper {

    OrganisationDTO toDTO(Organisation entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(OrganisationDTO dto, @MappingTarget Organisation entity);

    List<OrganisationDTO> toDTOList(List<Organisation> entities);


}
