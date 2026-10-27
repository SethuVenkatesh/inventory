package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.ItemDTO;
import com.project.sethu.inventory_store.entity.User;
import com.project.sethu.inventory_store.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organisations/{organisationId}/items")
@Tag(name = "Item Controller", description = "Controller for managing items within an organisation")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping()
    @Operation(summary = "Create an item", description = "Creates a new item under the specified organisation.")
    public ResponseEntity<APIResponse<ItemDTO>> create(
            @PathVariable UUID organisationId,
            @Validated({ItemDTO.OnCreate.class}) @RequestBody ItemDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(service.create(organisationId,dto,user)));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Update an item", description = "Updates the item details for the specified organisation and item ID.")
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
    @Operation(summary = "Get all items in an organisation", description = "Returns a list of all active items associated with the authenticated user and organisation.")
    public ResponseEntity<APIResponse<List<ItemDTO>>> getAll(
            @PathVariable UUID organisationId,
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(APIResponse.success(service.getAll(organisationId,user)));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Get item by ID", description = "Returns the item details for the specified organisation and item ID.")
    public ResponseEntity<APIResponse<ItemDTO>> getById(
            @PathVariable UUID organisationId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success(service.getById(organisationId,itemId,user)));
    }

    @DeleteMapping("{itemId}")
    @Operation(summary = "Delete an item", description = "Soft deletes the item for the specified organisation and item ID.")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable UUID organisationId,
            @PathVariable UUID itemId,
            @AuthenticationPrincipal User user) {
        service.softDelete(organisationId,itemId,user);
        return ResponseEntity.ok(APIResponse.deleted());
    }

}