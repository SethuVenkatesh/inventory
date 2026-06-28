package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.service.OrganisationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    private final OrganisationService service;

    public OrganisationController(OrganisationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<OrganisationDTO>>> getAll(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success(service.getAllOrganisation(user)));
    }

    @GetMapping("{id}")
    public ResponseEntity<APIResponse<OrganisationDTO>> getById(
            @PathVariable("id") UUID organisationId,
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(APIResponse.success(service.getById(user,organisationId)));
    }

    @PostMapping
    public ResponseEntity<APIResponse<OrganisationDTO>> create(
            @Validated({OrganisationDTO.OnCreate.class}) @RequestBody OrganisationDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(service.create(user,dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<OrganisationDTO>> update(
            @Validated({OrganisationDTO.OnUpdate.class}) @RequestBody OrganisationDTO dto,
            @PathVariable("id") UUID organisationId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success("Updated successfully", service.update(user, dto, organisationId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable("id") UUID organisationId,
            @AuthenticationPrincipal User user
    ) {
        service.softDelete(user,organisationId);
        return ResponseEntity.ok(APIResponse.deleted());
    }
}