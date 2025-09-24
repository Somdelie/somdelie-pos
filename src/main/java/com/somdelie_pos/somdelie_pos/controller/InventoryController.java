package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.InventoryService;
import com.somdelie_pos.somdelie_pos.payload.dto.InventoryDTO;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(
            @RequestBody InventoryDTO inventoryDTO
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.createInventory(inventoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @RequestBody InventoryDTO inventoryDTO
            , @PathVariable UUID id
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInventory(
            @PathVariable UUID id
    ) throws Exception {
        inventoryService.deleteInventory(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Inventory successfully deleted");

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/branch/{branchId}/product/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByProductAndBranchId(
            @PathVariable UUID productId, @PathVariable UUID branchId
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.getInventoryByProductIdAndBranchId(productId, branchId));
    }


    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByBranch(
            @PathVariable UUID branchId
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.getAllInventoryByBranch(branchId));
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDTO> getInventoryById(
            @PathVariable UUID inventoryId
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }


}
