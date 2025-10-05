package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Refund;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {

    // Fetch refunds by cashier entity and date range
    List<Refund> findByCashierAndCreatedAtBetween(User cashier, LocalDateTime from, LocalDateTime to);

    List<Refund> findByCashier(User cashier);

    List<Refund> findByShiftReportId(UUID shiftReportId);

    List<Refund> findByBranchId(UUID branchId);
}
