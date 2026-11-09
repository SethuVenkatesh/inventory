package com.project.sethu.inventory_store.repository;


import com.project.sethu.inventory_store.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {

    List<Organisation> findByIsActiveTrue();

    boolean existsByName(String name);

    Organisation findByIdAndIsActiveTrue(UUID organisationId);

    @Query("SELECT o.id FROM Organisation o WHERE o.isActive = false AND o.updatedAt <= :cutoff")
    List<UUID> findExpiredInactiveOrganisationIds(@Param("cutoff") OffsetDateTime cutoff);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Organisation o WHERE o.isActive = false AND o.updatedAt <= :cutoff")
    int deleteExpiredInactiveOrganisations(@Param("cutoff") OffsetDateTime cutoff);
}