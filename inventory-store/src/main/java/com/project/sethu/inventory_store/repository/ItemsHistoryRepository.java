package com.project.sethu.inventory_store.repository;

import com.project.sethu.inventory_store.entity.ItemsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemsHistoryRepository extends JpaRepository<ItemsHistory, UUID> {

    // All history for a specific item — latest first
    List<ItemsHistory> findByItemIdOrderByChangedAtDesc(UUID itemId);

    // All history for all items in an organisation
    List<ItemsHistory> findByOrganisationIdOrderByChangedAtDesc(UUID organisationId);

}