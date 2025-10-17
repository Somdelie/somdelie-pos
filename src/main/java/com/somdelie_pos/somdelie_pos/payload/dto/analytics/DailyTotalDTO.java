package com.somdelie_pos.somdelie_pos.payload.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyTotalDTO {
    private LocalDate date;
    private long ordersCount;
    private double totalRevenue;
}
