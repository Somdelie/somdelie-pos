package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.AnalyticsService;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.AnalyticsSummaryDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.DailyTotalDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.PaymentBreakdownDTO;
import com.somdelie_pos.somdelie_pos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;

    private static LocalDateTime startOf(LocalDate d) {
        return d.atStartOfDay();
    }

    private static LocalDateTime endOf(LocalDate d) {
        return d.plusDays(1).atStartOfDay();
    }

    private List<PaymentBreakdownDTO> mapPayments(List<Object[]> rows) {
        return rows.stream().map(r -> new PaymentBreakdownDTO(
                (PaymentType) r[0],
                ((Number) r[1]).longValue(),
                ((Number) r[2]).doubleValue())).collect(Collectors.toList());
    }

    private List<DailyTotalDTO> mapDaily(List<Object[]> rows) {
        return rows.stream().map(r -> new DailyTotalDTO(
                ((java.sql.Date) r[0]).toLocalDate(),
                ((Number) r[1]).longValue(),
                ((Number) r[2]).doubleValue() // totalRevenue
        )).collect(Collectors.toList());
    }

    @Override
    public AnalyticsSummaryDTO getCashierSummary(UUID cashierId, LocalDate from, LocalDate to) {
        LocalDateTime f = startOf(from), t = endOf(to);
        double revenue = orderRepository.sumRevenueByCashierBetween(cashierId, f, t);
        long count = orderRepository.countOrdersByCashierBetween(cashierId, f, t);
        var payments = mapPayments(orderRepository.paymentBreakdownByCashierBetween(cashierId, f, t));
        return AnalyticsSummaryDTO.builder().ordersCount(count).totalRevenue(revenue).payments(payments).build();
    }

    @Override
    public AnalyticsSummaryDTO getBranchSummary(UUID branchId, LocalDate from, LocalDate to) {
        LocalDateTime f = startOf(from), t = endOf(to);
        double revenue = orderRepository.sumRevenueByBranchBetween(branchId, f, t);
        long count = orderRepository.countOrdersByBranchBetween(branchId, f, t);
        var payments = mapPayments(orderRepository.paymentBreakdownByBranchBetween(branchId, f, t));
        return AnalyticsSummaryDTO.builder().ordersCount(count).totalRevenue(revenue).payments(payments).build();
    }

    @Override
    public AnalyticsSummaryDTO getStoreSummary(UUID storeId, LocalDate from, LocalDate to) {
        LocalDateTime f = startOf(from), t = endOf(to);
        double revenue = orderRepository.sumRevenueByStoreBetween(storeId, f, t);
        long count = orderRepository.countOrdersByStoreBetween(storeId, f, t);
        var payments = mapPayments(orderRepository.paymentBreakdownByStoreBetween(storeId, f, t));
        return AnalyticsSummaryDTO.builder().ordersCount(count).totalRevenue(revenue).payments(payments).build();
    }

    @Override
    public AnalyticsSummaryDTO getGlobalSummary(LocalDate from, LocalDate to) {
        LocalDateTime f = startOf(from), t = endOf(to);
        double revenue = orderRepository.sumRevenueBetween(f, t);
        long count = orderRepository.countOrdersBetween(f, t);
        var payments = mapPayments(orderRepository.paymentBreakdownBetween(f, t));
        return AnalyticsSummaryDTO.builder().ordersCount(count).totalRevenue(revenue).payments(payments).build();
    }

    @Override
    public List<DailyTotalDTO> getCashierDaily(UUID cashierId, LocalDate from, LocalDate to) {
        return mapDaily(orderRepository.dailyTotalsByCashierBetween(cashierId, startOf(from), endOf(to)));
    }

    @Override
    public List<DailyTotalDTO> getBranchDaily(UUID branchId, LocalDate from, LocalDate to) {
        return mapDaily(orderRepository.dailyTotalsByBranchBetween(branchId, startOf(from), endOf(to)));
    }

    @Override
    public List<DailyTotalDTO> getStoreDaily(UUID storeId, LocalDate from, LocalDate to) {
        return mapDaily(orderRepository.dailyTotalsByStoreBetween(storeId, startOf(from), endOf(to)));
    }

    @Override
    public List<DailyTotalDTO> getGlobalDaily(LocalDate from, LocalDate to) {
        return mapDaily(orderRepository.dailyTotalsBetween(startOf(from), endOf(to)));
    }
}
