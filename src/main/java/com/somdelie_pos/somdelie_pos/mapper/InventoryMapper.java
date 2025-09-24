package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Inventory;
import com.somdelie_pos.somdelie_pos.modal.Product;
import com.somdelie_pos.somdelie_pos.payload.dto.InventoryDTO;

public class InventoryMapper {

    public static InventoryDTO toDto(Inventory inventory) {
        return InventoryDTO.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .product(ProductMapper.toDTO(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .build();
    }

    public static Inventory toEntity(InventoryDTO inventoryDTO,
                                     Branch branch,
                                     Product product) {
        return Inventory.builder()
                .branch(branch)
                .product(product)
                .quantity(inventoryDTO.getQuantity())
                .build();

    }
}
