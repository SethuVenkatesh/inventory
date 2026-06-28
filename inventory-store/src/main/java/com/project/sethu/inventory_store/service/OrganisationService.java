package com.project.sethu.inventory_store.service;


import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.Organisation;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.entity.UserOrganisation;
import com.project.sethu.inventory_store.exception.DuplicateResourceException;
import com.project.sethu.inventory_store.exception.ResourceNotFoundException;
import com.project.sethu.inventory_store.mapper.OrganisationMapper;
import com.project.sethu.inventory_store.repository.OrganisationRepository;
import com.project.sethu.inventory_store.repository.UserOrganisationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganisationService {

    private final UserOrganisationRepository userOrganisationRepository;
    private final OrganisationRepository repository;
    private final OrganisationMapper mapper;

    public OrganisationService(UserOrganisationRepository userOrganisationRepository,OrganisationRepository repository, OrganisationMapper mapper) {
        this.userOrganisationRepository = userOrganisationRepository;
        this.repository = repository;
        this.mapper = mapper;
    }

    public OrganisationDTO getById(User user, UUID organisationId) {
        return mapper.toDTO(findOrThrow(user.getId(),organisationId));
    }

    public List<OrganisationDTO> getAllOrganisation(User user) {
        List<UserOrganisation> userOrgs =
                userOrganisationRepository.findActiveOrganisationsByUserId(user.getId());

        return userOrgs.stream().map(uo -> {
            OrganisationDTO dto = mapper.toDTO(uo.getOrganisation());
            dto.setIsPrimary(uo.getIsPrimary());   // mark which is primary
            return dto;
        }).collect(Collectors.toList());
    }

    public OrganisationDTO create(User user,OrganisationDTO dto) {
        Organisation org= new Organisation();
        mapper.toEntity(dto, org);
        org.setIsActive(true);
        org.setCreatedBy(user.getEmail());
        org.setUpdatedBy(user.getEmail());
        Organisation savedOrg = repository.save(org);

        UserOrganisation userOrg = new UserOrganisation();
        userOrg.setUser(user);
        userOrg.setOrganisation(savedOrg);
        userOrg.setIsPrimary(false);           // ← mark as primary
        userOrganisationRepository.save(userOrg);

        return mapper.toDTO(savedOrg);
    }

    public OrganisationDTO update(User user, OrganisationDTO dto,UUID organisationId ) {
        Organisation org = findOrThrow(user.getId(),organisationId);
        mapper.toEntity(dto, org);
        return mapper.toDTO(repository.save(org));
    }

    public void softDelete(User user, UUID organisationId) {
        Organisation org = findOrThrow(user.getId(),organisationId);
        org.setIsActive(false);
        repository.save(org);
    }

    private Organisation findOrThrow(UUID userId,UUID organisationId) {
        return userOrganisationRepository.findByUserIdAndOrganisationId(userId,organisationId)
                .map(UserOrganisation::getOrganisation)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation not found with id: " + organisationId));
    }


}