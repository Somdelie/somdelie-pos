package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.domain.OrderStatus;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {
    private UUID id;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID brandId;
    private CustomerDTO customer;
    private PaymentType paymentType;
    private OrderStatus status;  // ✅ Add this
    private List<OrderItemDTO> items;
    private UserDto cashier;
}
