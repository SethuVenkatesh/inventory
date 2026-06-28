package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.dto.OrganisationDTO;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.mapper.ItemMapper;
import com.project.sethu.inventory_store.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organisations/{organisationId}/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<APIResponse<ItemDTO>> create(
            @PathVariable UUID organisationId,
            @Validated({ItemDTO.OnCreate.class}) @RequestBody ItemDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(service.create(organisationId,dto,user)));
    }



    @PutMapping("/{itemId}")
    public ResponseEntity<APIResponse<ItemDTO>> update(
            @PathVariable UUID organisationId,
            @PathVariable UUID itemId,
            @Validated({ItemDTO.OnUpdate.class}) @RequestBody ItemDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(service.update(organisationId,itemId,dto,user)));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<ItemDTO>>> getAll(
            @PathVariable UUID organisationId,
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(APIResponse.success(service.getAll(organisationId,user)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<APIResponse<ItemDTO>> getById(
            @PathVariable UUID organisationId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success(service.getById(organisationId,itemId,user)));
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable UUID organisationId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal User user) {
        service.softDelete(organisationId,itemId,user);
        return ResponseEntity.ok(APIResponse.deleted());
    }


}