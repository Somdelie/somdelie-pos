package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.InventoryService;
import com.somdelie_pos.somdelie_pos.mapper.InventoryMapper;
import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Inventory;
import com.somdelie_pos.somdelie_pos.modal.Product;
import com.somdelie_pos.somdelie_pos.payload.dto.InventoryDTO;
import com.somdelie_pos.somdelie_pos.repository.BranchRepository;
import com.somdelie_pos.somdelie_pos.repository.InventoryRepository;
import com.somdelie_pos.somdelie_pos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;


    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) throws Exception {

        Branch branch = branchRepository.findById(inventoryDTO.getBranchId()).orElseThrow(
                () -> new Exception("Branch does not exist...")
        );

        Product product = productRepository.findById(inventoryDTO.getProductId()).orElseThrow(
                () -> new Exception("Product does not exist...")
        );

        Inventory inventory = InventoryMapper.toEntity(inventoryDTO, branch, product);
        Inventory inventorySaved = inventoryRepository.save(inventory);

        return  InventoryMapper.toDto(inventorySaved);
    }

    @Override
    public InventoryDTO updateInventory(UUID id, InventoryDTO inventoryDTO) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory not found!")
        );
        inventory.setQuantity(inventoryDTO.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(updatedInventory);
    }

    @Override
    public void deleteInventory(UUID inventoryId) throws Exception {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new Exception("Inventory does not exists...")
        );

        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDTO getInventoryById(UUID inventoryId) throws Exception {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new Exception("Inventory does not exists...")
        );
        return  InventoryMapper.toDto(inventory);
    }

    @Override
    public InventoryDTO getInventoryByProductIdAndBranchId(UUID productId, UUID branchId) throws Exception {
        Inventory inventory = inventoryRepository.findByProductIdAndBranchId(productId, branchId)
                .orElseThrow(() -> new Exception("Inventory not found for this product and branch"));
        return InventoryMapper.toDto(inventory);
    }


    @Override
    public List<InventoryDTO> getAllInventoryByBranch(UUID branchId) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(branchId);
        return inventories.stream()
                .map(InventoryMapper::toDto)
                .toList();
    }

}
