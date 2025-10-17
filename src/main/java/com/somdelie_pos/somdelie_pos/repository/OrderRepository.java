package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByCustomerId(UUID customerId);

    List<Order> findByBranchId(UUID branchId);

    List<Order> findByCashierId(UUID cashierId);

    List<Order> findByBranchIdAndCreatedAtBetween(UUID branchId,
            LocalDateTime from, LocalDateTime to);

    List<Order> findByCashierAndCreatedAtBetween(
            User cashier, LocalDateTime from, LocalDateTime to);

    List<Order> findTop5ByBranchIdOrderByCreatedAtDesc(UUID branchId);

    UUID id(UUID id);

    // Aggregations for summaries
    @Query("select coalesce(sum(o.totalAmount),0) from Order o where o.createdAt between :from and :to")
    Double sumRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select count(o) from Order o where o.createdAt between :from and :to")
    Long countOrdersBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select o.paymentType, count(o), coalesce(sum(o.totalAmount),0) from Order o where o.createdAt between :from and :to group by o.paymentType")
    List<Object[]> paymentBreakdownBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select date(o.createdAt), count(o), coalesce(sum(o.totalAmount),0) from Order o where o.createdAt between :from and :to group by date(o.createdAt) order by date(o.createdAt)")
    List<Object[]> dailyTotalsBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Scopes: cashier, branch, store
    @Query("select coalesce(sum(o.totalAmount),0) from Order o where o.cashier.id = :cashierId and o.createdAt between :from and :to")
    Double sumRevenueByCashierBetween(@Param("cashierId") UUID cashierId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select count(o) from Order o where o.cashier.id = :cashierId and o.createdAt between :from and :to")
    Long countOrdersByCashierBetween(@Param("cashierId") UUID cashierId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select o.paymentType, count(o), coalesce(sum(o.totalAmount),0) from Order o where o.cashier.id = :cashierId and o.createdAt between :from and :to group by o.paymentType")
    List<Object[]> paymentBreakdownByCashierBetween(@Param("cashierId") UUID cashierId,
            @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select date(o.createdAt), count(o), coalesce(sum(o.totalAmount),0) from Order o where o.cashier.id = :cashierId and o.createdAt between :from and :to group by date(o.createdAt) order by date(o.createdAt)")
    List<Object[]> dailyTotalsByCashierBetween(@Param("cashierId") UUID cashierId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select coalesce(sum(o.totalAmount),0) from Order o where o.branch.id = :branchId and o.createdAt between :from and :to")
    Double sumRevenueByBranchBetween(@Param("branchId") UUID branchId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select count(o) from Order o where o.branch.id = :branchId and o.createdAt between :from and :to")
    Long countOrdersByBranchBetween(@Param("branchId") UUID branchId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select o.paymentType, count(o), coalesce(sum(o.totalAmount),0) from Order o where o.branch.id = :branchId and o.createdAt between :from and :to group by o.paymentType")
    List<Object[]> paymentBreakdownByBranchBetween(@Param("branchId") UUID branchId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select date(o.createdAt), count(o), coalesce(sum(o.totalAmount),0) from Order o where o.branch.id = :branchId and o.createdAt between :from and :to group by date(o.createdAt) order by date(o.createdAt)")
    List<Object[]> dailyTotalsByBranchBetween(@Param("branchId") UUID branchId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select coalesce(sum(o.totalAmount),0) from Order o where o.branch.store.id = :storeId and o.createdAt between :from and :to")
    Double sumRevenueByStoreBetween(@Param("storeId") UUID storeId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select count(o) from Order o where o.branch.store.id = :storeId and o.createdAt between :from and :to")
    Long countOrdersByStoreBetween(@Param("storeId") UUID storeId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select o.paymentType, count(o), coalesce(sum(o.totalAmount),0) from Order o where o.branch.store.id = :storeId and o.createdAt between :from and :to group by o.paymentType")
    List<Object[]> paymentBreakdownByStoreBetween(@Param("storeId") UUID storeId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("select date(o.createdAt), count(o), coalesce(sum(o.totalAmount),0) from Order o where o.branch.store.id = :storeId and o.createdAt between :from and :to group by date(o.createdAt) order by date(o.createdAt)")
    List<Object[]> dailyTotalsByStoreBetween(@Param("storeId") UUID storeId, @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
