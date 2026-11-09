package com.project.sethu.inventory_store.repository;


import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    List<Item> findByOrganisationIdAndIsActiveTrue(UUID organisationId);

    Item findByNameAndOrganisationIdAndIsActiveTrue(String name, UUID organisationId);

    Item findByIdAndOrganisationIdAndIsActiveTrue(UUID id, UUID organisationId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Item i WHERE i.isActive = false AND i.updatedAt <= :cutoff")
    int deleteExpiredInactiveItems(@Param("cutoff") OffsetDateTime cutoff);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Item i WHERE i.organisation.id IN :organisationIds")
    int deleteByOrganisationIds(@Param("organisationIds") Collection<UUID> organisationIds);
}
