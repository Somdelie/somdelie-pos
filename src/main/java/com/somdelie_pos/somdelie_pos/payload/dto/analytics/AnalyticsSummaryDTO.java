package com.somdelie_pos.somdelie_pos.payload.dto.analytics;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AnalyticsSummaryDTO {
    private long ordersCount;
    private double totalRevenue;
    private List<PaymentBreakdownDTO> payments;
}
