package com.project.sethu.inventory_store.service;


import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.Organisation;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.exception.DuplicateResourceException;
import com.project.sethu.inventory_store.exception.ResourceNotFoundException;
import com.project.sethu.inventory_store.mapper.OrganisationMapper;
import com.project.sethu.inventory_store.repository.OrganisationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganisationService {

    private final OrganisationRepository repository;
    private final OrganisationMapper mapper;

    public OrganisationService(OrganisationRepository repository, OrganisationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<OrganisationDTO> getAll() {
        return mapper.toDTOList(repository.findByIsActiveTrue());
    }

    public OrganisationDTO getById(User user) {
        UUID id = getByUser(user);
        return mapper.toDTO(findOrThrow(id));
    }

    public OrganisationDTO create(OrganisationDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Organisation already exists: " + dto.getName());
        }
        Organisation org= new Organisation();
        mapper.toEntity(dto, org);
        org.setIsActive(true);
        org.setUpdatedBy("");
        return mapper.toDTO(repository.save(org));
    }

    public OrganisationDTO update(User user, OrganisationDTO dto) {
        UUID id = getByUser(user);
        Organisation org = findOrThrow(id);
        mapper.toEntity(dto, org);
        return mapper.toDTO(repository.save(org));
    }

    public void softDelete(User user) {
        UUID id = getByUser(user);
        Organisation org = findOrThrow(id);
        org.setIsActive(false);
        repository.save(org);
    }

    private Organisation findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisation", id));
    }

    public UUID getByUser(User user) {
        if (user.getOrganisation() == null) {
            throw new ResourceNotFoundException("No organisation linked to this user");
        }
        return user.getOrganisation().getId();
    }
}