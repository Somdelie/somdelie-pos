package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.OrderItem;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderItemDTO;

public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderItem item) {
        if (item == null) return null;
        return OrderItemDTO.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .sellingPrice(item.getProduct() != null ? item.getProduct().getSellingPrice() : null)
                .product(ProductMapper.toDTO(item.getProduct()))
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .build();
    }
}
