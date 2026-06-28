package com.project.sethu.inventory_store.service;

import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.entity.Item;
import com.project.sethu.inventory_store.entity.Organisation;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.exception.DuplicateResourceException;
import com.project.sethu.inventory_store.exception.ResourceNotFoundException;
import com.project.sethu.inventory_store.mapper.ItemMapper;
import com.project.sethu.inventory_store.repository.ItemRepository;
import com.project.sethu.inventory_store.repository.OrganisationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ItemService {
    private final OrganisationRepository organisationRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(OrganisationRepository organisationRepository, ItemRepository itemRepository, ItemMapper itemMapper) {
        this.organisationRepository = organisationRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }



    public ItemDTO create(UUID organisationId, ItemDTO dto, User user) {
        Organisation organisation= organisationRepository.findByIdAndIsActiveTrue(organisationId);
        if(organisation==null) throw new ResourceNotFoundException("Organisation not found with id: " + organisationId);
        boolean itemExsist = itemRepository.existsByNameAndOrganisationIdAndIsActiveTrue(dto.getName(),organisationId);
        if (itemExsist) {
            throw new DuplicateResourceException("Item with name '" + dto.getName() + "' already exists for this organisation.");
        }
        Item item = new Item();;
        itemMapper.toEntity(dto,item);
        item.setCreatedBy(user.getEmail());
        item.setUpdatedBy(user.getEmail());
        item.setOrganisation(organisation);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDTO(savedItem);

    }


    public ItemDTO update(UUID organisationId, UUID itemId, ItemDTO dto, User user) {
        Organisation organisation= organisationRepository.findByIdAndIsActiveTrue(organisationId);
        if(organisation==null) throw new ResourceNotFoundException("Organisation not found with id: " + organisationId);
        Item item = itemRepository.findByIdAndOrganisationIdAndIsActiveTrue(itemId,organisationId);
        if (item == null) {
            throw new ResourceNotFoundException("Item not found with id: " + itemId);
        }
        boolean itemExsist = itemRepository.existsByNameAndOrganisationIdAndIsActiveTrue(dto.getName(),organisationId);
        if (itemExsist) {
            throw new DuplicateResourceException("Item with name '" + dto.getName() + "' already exists for this organisation.");
        }
        itemMapper.toEntity(dto,item);
        item.setOrganisation(organisation);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDTO(savedItem);
    }


    public List<ItemDTO> getAll(UUID organisationId, User user)  {
        Organisation organisation= organisationRepository.findByIdAndIsActiveTrue(organisationId);
        if(organisation==null) throw new ResourceNotFoundException("Organisation not found with id: " + organisationId);
        List<Item> items =itemRepository.findByOrganisationIdAndIsActiveTrue(organisationId);
        return itemMapper.toDTOList(items);
    }

    public ItemDTO getById(UUID organisationId,UUID itemId, User user) {
        Organisation organisation= organisationRepository.findByIdAndIsActiveTrue(organisationId);
        if(organisation==null) throw new ResourceNotFoundException("Organisation not found with id: " + organisationId);
        Item item =itemRepository.findByIdAndOrganisationIdAndIsActiveTrue(itemId,organisationId);
        if (item == null) {
            throw new ResourceNotFoundException("Item not found with id: " + itemId);
        }
        return itemMapper.toDTO(item);
    }

    public void softDelete(UUID organisationId, UUID itemId, User user) {
        Item item = itemRepository.findByIdAndOrganisationIdAndIsActiveTrue(itemId, organisationId);
        if (item == null) {
            throw new ResourceNotFoundException("Item not found with id: " + itemId);
        }
        item.setIsActive(false);
        itemRepository.save(item);
    }

}