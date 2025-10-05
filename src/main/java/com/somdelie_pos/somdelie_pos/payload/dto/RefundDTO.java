package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RefundDTO {

    private UUID id;


    private OrderDTO order;
    private UUID orderId;

    private String reason;

    private Double amount;

//    private ShiftReport shiftReport;
    private UUID shiftReportId;

    private UserDto cashier;
    private String cashierName;

    private BranchDTO branch;
    private UUID branchId;

    private PaymentType paymentType;

    private LocalDateTime createdAt;
}
