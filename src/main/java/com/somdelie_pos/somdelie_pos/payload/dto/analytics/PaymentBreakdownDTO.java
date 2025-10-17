package com.somdelie_pos.somdelie_pos.payload.dto.analytics;

import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentBreakdownDTO {
    private PaymentType paymentType;
    private long count;
    private double amount;
}
