package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductDTO {
    private UUID id;              // Changed from Long to UUID
    private String name;
    private String sku;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private String brand;
    private String image;
    private UUID categoryId;      // Changed from Long to UUID
    private UUID storeId;         // Changed from Long to UUID
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}