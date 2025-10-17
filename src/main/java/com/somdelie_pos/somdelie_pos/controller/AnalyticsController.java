package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.AnalyticsService;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.AnalyticsSummaryDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.analytics.DailyTotalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/cashier/{cashierId}/summary")
    public ResponseEntity<AnalyticsSummaryDTO> cashierSummary(
            @PathVariable UUID cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getCashierSummary(cashierId, from, to));
    }

    @GetMapping("/branch/{branchId}/summary")
    public ResponseEntity<AnalyticsSummaryDTO> branchSummary(
            @PathVariable UUID branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getBranchSummary(branchId, from, to));
    }

    @GetMapping("/store/{storeId}/summary")
    public ResponseEntity<AnalyticsSummaryDTO> storeSummary(
            @PathVariable UUID storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getStoreSummary(storeId, from, to));
    }

    @GetMapping("/global/summary")
    public ResponseEntity<AnalyticsSummaryDTO> globalSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getGlobalSummary(from, to));
    }

    @GetMapping("/cashier/{cashierId}/daily")
    public ResponseEntity<List<DailyTotalDTO>> cashierDaily(
            @PathVariable UUID cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getCashierDaily(cashierId, from, to));
    }

    @GetMapping("/branch/{branchId}/daily")
    public ResponseEntity<List<DailyTotalDTO>> branchDaily(
            @PathVariable UUID branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getBranchDaily(branchId, from, to));
    }

    @GetMapping("/store/{storeId}/daily")
    public ResponseEntity<List<DailyTotalDTO>> storeDaily(
            @PathVariable UUID storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getStoreDaily(storeId, from, to));
    }

    @GetMapping("/global/daily")
    public ResponseEntity<List<DailyTotalDTO>> globalDaily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws Exception {
        return ResponseEntity.ok(analyticsService.getGlobalDaily(from, to));
    }
}
