package com.project.sethu.inventory_store.repository;


import com.project.sethu.inventory_store.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {

    List<Organisation> findByIsActiveTrue();

    boolean existsByName(String name);

    Organisation findByIdAndIsActiveTrue(UUID organisationId);
}