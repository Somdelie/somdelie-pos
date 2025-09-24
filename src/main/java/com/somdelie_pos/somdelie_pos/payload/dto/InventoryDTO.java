package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InventoryDTO {

    private UUID id;

    private UUID branchId;

    private BranchDTO branch;

    private UUID productId;

    private ProductDTO product;

    private Integer quantity;

    private LocalDateTime lastUpdate;
}
