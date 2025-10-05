package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByCustomerId(UUID customerId);
    List<Order> findByBranchId(UUID  branchId);
    List<Order> findByCashierId(UUID cashierId);
    List<Order> findByBranchIdAndCreatedAtBetween(UUID branchId,
                                                  LocalDateTime from, LocalDateTime to);
    List<Order> findByCashierAndCreatedAtBetween(
            User cashier, LocalDateTime from, LocalDateTime to
    );
    List<Order> findTop5ByBranchIdOrderByCreatedAtDesc(UUID branchId);

    UUID id(UUID id);
}
