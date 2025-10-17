package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.payload.dto.analytics.AnalyticsSummaryDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.DailyTotalDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AnalyticsService {
    AnalyticsSummaryDTO getCashierSummary(UUID cashierId, LocalDate from, LocalDate to) throws Exception;

    AnalyticsSummaryDTO getBranchSummary(UUID branchId, LocalDate from, LocalDate to) throws Exception;

    AnalyticsSummaryDTO getStoreSummary(UUID storeId, LocalDate from, LocalDate to) throws Exception;

    AnalyticsSummaryDTO getGlobalSummary(LocalDate from, LocalDate to) throws Exception;

    List<DailyTotalDTO> getCashierDaily(UUID cashierId, LocalDate from, LocalDate to) throws Exception;

    List<DailyTotalDTO> getBranchDaily(UUID branchId, LocalDate from, LocalDate to) throws Exception;

    List<DailyTotalDTO> getStoreDaily(UUID storeId, LocalDate from, LocalDate to) throws Exception;

    List<DailyTotalDTO> getGlobalDaily(LocalDate from, LocalDate to) throws Exception;
}
