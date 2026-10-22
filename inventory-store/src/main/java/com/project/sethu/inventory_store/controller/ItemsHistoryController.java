package com.project.sethu.inventory_store.controller;

import com.project.sethu.inventory_store.dto.APIResponse;
import com.project.sethu.inventory_store.dto.ItemsHistoryDTO;
import com.project.sethu.inventory_store.service.ItemsHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items/history")
@Tag(name = "Items History Controller", description = "Controller for retrieving item history records")
public class ItemsHistoryController {

    private final ItemsHistoryService historyService;

    public ItemsHistoryController(ItemsHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Get item history by item ID", description = "Returns the history records for the specified item.")
    public ResponseEntity<APIResponse<List<ItemsHistoryDTO>>> getByItem(
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(
                APIResponse.success(historyService.getByItem(itemId)));
    }

}
