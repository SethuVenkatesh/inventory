package com.project.sethu.inventory_store.repository;


import com.project.sethu.inventory_store.entity.UserOrganisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOrganisationRepository extends JpaRepository<UserOrganisation, UUID> {

    List<UserOrganisation> findByUserId(UUID userId);

    List<UserOrganisation> findByOrganisationId(UUID organisationId);

    Optional<UserOrganisation> findByUserIdAndOrganisationId(UUID userId, UUID organisationId);

    boolean existsByUserIdAndOrganisationId(UUID userId, UUID organisationId);

    // Get all organisations for a user
    @Query("""
        SELECT uo FROM UserOrganisation uo
        JOIN FETCH uo.organisation o
        WHERE uo.user.id = :userId
        AND o.isActive = true
    """)
    List<UserOrganisation> findActiveOrganisationsByUserId(@Param("userId") UUID userId);

}
