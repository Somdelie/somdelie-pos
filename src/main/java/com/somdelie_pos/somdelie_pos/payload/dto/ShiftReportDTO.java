package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.modal.PaymentSummaries;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShiftReportDTO {

    private UUID id;

    private LocalTime shiftStart;
    private LocalTime shiftEnd;

    private Double totalSales;
    private Double totalRefunds;
    private Double netSale;
    private Integer totalOrders; // ✅ changed from Double → Integer

    private UserDto cashier;
    private UUID cashierId;

    private BranchDTO branch;
    private UUID branchId;

    private List<PaymentSummaries> paymentSummaries;

    private List<ProductDTO> topSellProducts;

    private List<OrderDTO> recentOrders;

    private List<RefundDTO> refunds;
}
