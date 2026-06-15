package com.project.sethu.inventory_store.mapper;

import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.entity.Item;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO toDTO(Item entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(ItemDTO dto, @MappingTarget Item entity);

    List<ItemDTO> toDTOList(List<Item> entities);

}
