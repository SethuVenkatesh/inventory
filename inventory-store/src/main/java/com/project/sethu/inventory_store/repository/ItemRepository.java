package com.project.sethu.inventory_store.repository;


import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {



    List<Item> findByOrganisationIdAndIsActiveTrue(UUID organisationId);

    boolean existsByNameAndOrganisationIdAndIsActiveTrue(String name, UUID organisationId);

    Item findByIdAndOrganisationIdAndIsActiveTrue(UUID id, UUID organisationId);


}
