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
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<APIResponse<ItemDTO>> create(
            @Validated({ItemDTO.OnCreate.class}) @RequestBody ItemDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.created(service.create(dto,user)));
    }



    @PutMapping()
    public ResponseEntity<APIResponse<ItemDTO>> update(
            @RequestParam(name = "itemId",required = true) UUID itemId,
            @Validated({ItemDTO.OnUpdate.class}) @RequestBody ItemDTO dto,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(service.update(dto,user,itemId)));
    }

    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<ItemDTO>>> getAll(
            @AuthenticationPrincipal User user
    )
    {
        return ResponseEntity.ok(APIResponse.success(service.getAll(user)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<ItemDTO>> getById(
            @RequestParam(name = "itemId",required = true) UUID itemId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(APIResponse.success(service.getById(user,itemId)));
    }

    @DeleteMapping("")
    public ResponseEntity<APIResponse<Void>> delete(
            @RequestParam(name = "itemId",required = true) UUID itemId,
            @AuthenticationPrincipal User user) {
        service.softDelete(user, itemId);
        return ResponseEntity.ok(APIResponse.deleted());
    }


}