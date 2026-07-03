package com.project.sethu.inventory_store.mapper;

import com.project.sethu.inventory_store.dto.ItemsHistoryDTO;
import com.project.sethu.inventory_store.entity.ItemsHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemsHistoryMapper {

    ItemsHistoryDTO toDTO(ItemsHistory entity);           // MapStruct handles it

    List<ItemsHistoryDTO> toDTOList(List<ItemsHistory> entities);
}