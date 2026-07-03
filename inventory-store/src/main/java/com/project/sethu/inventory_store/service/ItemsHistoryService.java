package com.project.sethu.inventory_store.service;


import com.project.sethu.inventory_store.dto.ItemsHistoryDTO;
import com.project.sethu.inventory_store.exception.ResourceNotFoundException;
import com.project.sethu.inventory_store.mapper.ItemsHistoryMapper;
import com.project.sethu.inventory_store.repository.ItemRepository;
import com.project.sethu.inventory_store.repository.ItemsHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ItemsHistoryService {

    private final ItemsHistoryRepository historyRepository;
    private final ItemRepository itemRepository;
    private final ItemsHistoryMapper mapper;

    public ItemsHistoryService(ItemsHistoryRepository historyRepository,
                               ItemRepository itemRepository,
                               ItemsHistoryMapper mapper) {
        this.historyRepository = historyRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    // All history for a specific item
    public List<ItemsHistoryDTO> getByItem(UUID itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", itemId));
        return mapper.toDTOList(
                historyRepository.findByItemIdOrderByChangedAtDesc(itemId));
    }

    // All history for all items in an organisation
    public List<ItemsHistoryDTO> getByOrganisation(UUID orgId) {
        return mapper.toDTOList(
                historyRepository.findByOrganisationIdOrderByChangedAtDesc(orgId));
    }

}