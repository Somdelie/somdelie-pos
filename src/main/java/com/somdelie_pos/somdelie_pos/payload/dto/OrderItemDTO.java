package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemDTO {
    private UUID id;
    private Integer quantity;
    private Double price;
    private Double sellingPrice;
    private ProductDTO product;
    private UUID productId;
}
