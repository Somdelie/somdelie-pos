package com.somdelie_pos.somdelie_pos.controller;


import com.somdelie_pos.somdelie_pos.Service.RefundService;
import com.somdelie_pos.somdelie_pos.payload.dto.RefundDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // Create a new refund
    @PostMapping
    public ResponseEntity<RefundDTO> createRefund(@RequestBody RefundDTO refundDTO) throws Exception {
        RefundDTO createdRefund = refundService.createRefund(refundDTO);
        return ResponseEntity.ok(createdRefund);
    }

    // Get all refunds
    @GetMapping
    public ResponseEntity<List<RefundDTO>> getAllRefunds() throws Exception {
        List<RefundDTO> refunds = refundService.getAllRefunds();
        return ResponseEntity.ok(refunds);
    }

    // Get refund by ID
    @GetMapping("/{id}")
    public ResponseEntity<RefundDTO> getRefundById(@PathVariable UUID id) throws Exception {
        RefundDTO refund = refundService.getRefundById(id);
        return ResponseEntity.ok(refund);
    }

    // Get refunds by cashier
    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByCashier(@PathVariable UUID cashierId) throws Exception {
        List<RefundDTO> refunds = refundService.getRefundByCashierId(cashierId);
        return ResponseEntity.ok(refunds);
    }

    // Get refunds by branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByBranch(@PathVariable UUID branchId) throws Exception {
        List<RefundDTO> refunds = refundService.getRefundByBranchId(branchId);
        return ResponseEntity.ok(refunds);
    }

    // Get refunds by shift report
    @GetMapping("/shift/{shiftReportId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByShiftReport(@PathVariable UUID shiftReportId) throws Exception {
        List<RefundDTO> refunds = refundService.getRefundByShiftReportId(shiftReportId);
        return ResponseEntity.ok(refunds);
    }

    // Get refunds by cashier and date range
    @GetMapping("/cashier/{cashierId}/range")
    public ResponseEntity<List<RefundDTO>> getRefundsByCashierAndDateRange(
            @PathVariable UUID cashierId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) throws Exception {
        List<RefundDTO> refunds = refundService.getRefundsByCashierAndDateRange(cashierId, startDate, endDate);
        return ResponseEntity.ok(refunds);
    }

    // Delete a refund
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(@PathVariable UUID id) throws Exception {
        refundService.deleteRefund(id);
        return ResponseEntity.noContent().build();
    }
}
