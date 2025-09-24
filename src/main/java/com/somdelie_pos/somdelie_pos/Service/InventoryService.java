package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.payload.dto.InventoryDTO;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception;
    InventoryDTO updateInventory(UUID id, InventoryDTO inventoryDTO) throws Exception;

    void deleteInventory(UUID inventoryId) throws Exception;
    InventoryDTO getInventoryById(UUID inventoryId) throws Exception;
    InventoryDTO getInventoryByProductIdAndBranchId(UUID productId, UUID branchId) throws Exception;
    List<InventoryDTO> getAllInventoryByBranch(UUID branchId);
}
