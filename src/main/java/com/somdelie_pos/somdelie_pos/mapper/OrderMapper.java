package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .brandId(order.getBranch() != null ? order.getBranch().getId() : null)
                .cashier(UserMapper.toDto(order.getCashier()))
                .status(order.getStatus())  // ✅ Add this
                .customer(CustomerMapper.toDTO(order.getCustomer()))
                .paymentType(order.getPaymentType())
                .items(order.getItems() != null ?
                        order.getItems().stream().map(OrderItemMapper::toDTO).collect(Collectors.toList())
                        : null)
                .build();
    }
}
