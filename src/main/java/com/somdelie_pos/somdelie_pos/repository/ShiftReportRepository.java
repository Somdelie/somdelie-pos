package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.ShiftReport;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, UUID> {

    // Remove 'throws Exception' - repository methods shouldn't throw checked exceptions
    List<ShiftReport> findByCashierId(UUID cashierId);

    List<ShiftReport> findByBranchId(UUID branchId);

    // Remove 'throws Exception'
    Optional<ShiftReport> findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(User cashier);

    // Fixed parameter names to be more clear
    Optional<ShiftReport> findByCashierAndShiftStartBetween(
            User cashier, LocalDateTime startTime, LocalDateTime endTime
    );

    // Alternative approach using @Query annotation for more complex queries
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.cashier = :cashier AND sr.shiftStart BETWEEN :startTime AND :endTime")
    Optional<ShiftReport> findByCashierAndShiftStartBetweenQuery(
            @Param("cashier") User cashier,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Additional useful methods for better querying
    @Query("SELECT sr FROM ShiftReport sr WHERE sr.cashier.id = :cashierId ORDER BY sr.shiftStart DESC")
    List<ShiftReport> findByCashierIdOrderByShiftStartDesc(@Param("cashierId") UUID cashierId);

    @Query("SELECT sr FROM ShiftReport sr WHERE sr.branch.id = :branchId ORDER BY sr.shiftStart DESC")
    List<ShiftReport> findByBranchIdOrderByShiftStartDesc(@Param("branchId") UUID branchId);
}