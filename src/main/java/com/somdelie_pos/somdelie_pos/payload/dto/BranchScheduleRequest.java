package com.somdelie_pos.somdelie_pos.payload.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 * Request payload to update a Branch schedule: working days and opening/closing
 * times.
 * Days are case-insensitive; allowed values: MONDAY..SUNDAY.
 */
@Data
public class BranchScheduleRequest {
    private List<String> workingDays;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
