package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.ShiftReportService;
import com.somdelie_pos.somdelie_pos.payload.dto.ShiftReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shift-reports")
public class ShiftReportController {

    private final ShiftReportService shiftReportService;

    @PostMapping("/start")
    public ResponseEntity<?> startShift() {
        try {
            ShiftReportDTO result = shiftReportService.startShift();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Return detailed error information including stack trace
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to start shift");
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now().toString());

            // Get the root cause
            Throwable rootCause = e;
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }
            error.put("rootCause", rootCause.getClass().getSimpleName() + ": " + rootCause.getMessage());

            // Include stack trace for debugging
            error.put("stackTrace", java.util.Arrays.toString(e.getStackTrace()));

            return ResponseEntity.badRequest().body(error);
        }
    }

    @PatchMapping("/end")
    public ResponseEntity<ShiftReportDTO> endShift(
            @RequestParam(required = false) UUID shiftReportId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime shiftEnd
    ) throws Exception {
        // If shiftEnd is not provided, use current time
        LocalDateTime endTime = shiftEnd != null ? shiftEnd : LocalDateTime.now();

        return ResponseEntity.ok(
                shiftReportService.endShift(shiftReportId, endTime)
        );
    }

    @GetMapping("/current")
    public ResponseEntity<ShiftReportDTO> getCurrentShiftProgress(
            @RequestParam(required = false) UUID cashierId
    ) throws Exception {
        return ResponseEntity.ok(
                shiftReportService.getCurrentShiftProgress(cashierId)
        );
    }

    @GetMapping("/cashier/{cashierId}/by-date")
    public ResponseEntity<ShiftReportDTO> getShiftReportByDate(
            @PathVariable UUID cashierId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) throws Exception {
        return ResponseEntity.ok(
                shiftReportService.getShiftByCashierAndDate(cashierId, date)
        );
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<ShiftReportDTO>> getShiftReportByCashier(
            @PathVariable UUID cashierId
    ) throws Exception {
        return ResponseEntity.ok(
                shiftReportService.getShiftReportsByCashierId(cashierId)
        );
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<ShiftReportDTO>> getShiftReportByBranch(
            @PathVariable UUID branchId
    ) throws Exception {
        return ResponseEntity.ok(
                shiftReportService.getShiftReportsByBranchId(branchId)
        );
    }

    // Keep this mapping last as it's a catch-all pattern
    @GetMapping("/shift/{shiftId}")
    public ResponseEntity<ShiftReportDTO> getShiftReportByShiftId(
            @PathVariable UUID shiftId
    ) throws Exception {
        return ResponseEntity.ok(
                shiftReportService.getShiftReportById(shiftId)
        );
    }

    // Temporary debug endpoint - remove after fixing the issue
    @GetMapping("/debug/refunds")
    public ResponseEntity<?> debugRefunds() {
        try {
            Map<String, Object> debugInfo = new HashMap<>();

            // Get all refunds (for debugging)
            // You'll need to add this method to your RefundRepository:
            // List<Refund> findAll();

            debugInfo.put("timestamp", LocalDateTime.now().toString());
            debugInfo.put("message", "Check console for detailed debug information");

            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Debug failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}