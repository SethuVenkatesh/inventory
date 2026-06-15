package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.service.OrganisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//    @GetMapping("/all")
//    public ResponseEntity<APIResponse<List<OrganisationDTO>>> getAll() {
//        return ResponseEntity.ok(APIResponse.success(service.getAll()));
//    }

    @GetMapping("")
    public ResponseEntity<APIResponse<OrganisationDTO>> getById(
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(APIResponse.success(service.getById(user)));
    }

//    @PostMapping
//    public ResponseEntity<APIResponse<OrganisationDTO>> create(@Validated({OrganisationDTO.OnCreate.class}) @RequestBody OrganisationDTO dto) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(APIResponse.created(service.create(dto)));
//    }

    @PutMapping("")
    public ResponseEntity<APIResponse<OrganisationDTO>> update(
            @Validated({OrganisationDTO.OnUpdate.class}) @RequestBody OrganisationDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success("Updated successfully", service.update(user, dto)));
    }

//    @DeleteMapping("")
//    public ResponseEntity<APIResponse<Void>> delete(
//            @RequestParam(name = "organisationId",required = true) UUID organisationId,
//            @AuthenticationPrincipal User user
//    ) {
//        service.softDelete(user);
//        return ResponseEntity.ok(APIResponse.deleted());
//    }
}