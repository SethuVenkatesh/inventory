package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.ItemsHistoryDTO;
import com.project.sethu.inventory_store.service.ItemsHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items/history")
public class ItemsHistoryController {

    private final ItemsHistoryService historyService;

    public ItemsHistoryController(ItemsHistoryService historyService) {
        this.historyService = historyService;
    }

    // GET all history for a specific item
    @GetMapping("/{itemId}")
    public ResponseEntity<APIResponse<List<ItemsHistoryDTO>>> getByItem(
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(
                APIResponse.success(historyService.getByItem(itemId)));
    }


}
