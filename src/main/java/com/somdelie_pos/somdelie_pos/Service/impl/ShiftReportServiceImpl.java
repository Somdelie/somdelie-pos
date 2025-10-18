package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.ShiftReportService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException;
import com.somdelie_pos.somdelie_pos.mapper.ShiftReportMapper;
import com.somdelie_pos.somdelie_pos.modal.*;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductWithQuantity;
import com.somdelie_pos.somdelie_pos.payload.dto.ShiftReportDTO;
import com.somdelie_pos.somdelie_pos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftReportServiceImpl implements ShiftReportService {

    private final ShiftReportRepository shiftReportRepository;
    private final UserService userService;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public ShiftReportDTO startShift() throws Exception {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                throw new Exception("Current user not found");
            }

            LocalDateTime shiftStart = LocalDateTime.now();
            LocalDateTime startOfDay = shiftStart.withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = shiftStart.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

            Optional<ShiftReport> existing = shiftReportRepository.findByCashierAndShiftStartBetween(
                    currentUser, startOfDay, endOfDay);

            if (existing.isPresent()) {
                throw new Exception("ShiftReport already started for today!");
            }

            Branch branch = currentUser.getBranch();
            if (branch == null) {
                throw new Exception("User branch not found");
            }

            ShiftReport shiftReport = ShiftReport.builder()
                    .cashier(currentUser)
                    .shiftStart(shiftStart)
                    .branch(branch)
                    .build();

            ShiftReport savedReport = shiftReportRepository.save(shiftReport);

            return ShiftReportMapper.toDTO(savedReport);
        } catch (Exception e) {
            // Log the actual error for debugging
            throw new Exception("Failed to start shift: " + e.getMessage(), e);
        }
    }

    @Override
    public ShiftReportDTO endShift(UUID shiftReportId, LocalDateTime shiftEnd) throws Exception {
        User currentUser = userService.getCurrentUser();

        // Resolve the active shift to end: prefer provided ID, else fallback to latest
        // active for current user
        ShiftReport shiftReport;
        if (shiftReportId != null) {
            shiftReport = shiftReportRepository.findById(shiftReportId)
                    .orElseThrow(() -> new ResourceNotFoundException("Shift report not found"));
            // Ensure the shift belongs to the current user
            if (shiftReport.getCashier() == null || !shiftReport.getCashier().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("You can only end your own active shift");
            }
            if (shiftReport.getShiftEnd() != null) {
                throw new IllegalStateException("Shift is already ended");
            }
        } else {
            shiftReport = shiftReportRepository
                    .findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("No active shift found for this cashier"));
        }

        // Use provided shiftEnd or current time if null
        LocalDateTime endTime = (shiftEnd != null) ? shiftEnd : LocalDateTime.now();
        // Guard against an end time before the start
        if (endTime.isBefore(shiftReport.getShiftStart())) {
            throw new IllegalArgumentException("Shift end time cannot be before shift start time");
        }

        shiftReport.setShiftEnd(endTime);

        // Calculate within [shiftStart, endTime]
        List<Refund> refunds = refundRepository.findByCashierAndCreatedAtBetween(
                currentUser, shiftReport.getShiftStart(), endTime);

        double totalRefunds = refunds.stream()
                .mapToDouble(refund -> refund.getAmount() != null ? refund.getAmount() : 0.0)
                .sum();

        List<Order> orders = orderRepository.findByCashierAndCreatedAtBetween(
                currentUser, shiftReport.getShiftStart(), endTime);

        double totalSales = orders.stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                .sum();

        int totalOrders = orders.size();
        double netSale = totalSales - totalRefunds;

        shiftReport.setNetSale(netSale);
        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setTopSellProducts(getTopSellingProducts(orders));
        shiftReport.setPaymentSummaries(getPaymentSummaries(orders, totalSales));
        shiftReport.setRefunds(refunds);

        ShiftReport savedReport = shiftReportRepository.save(shiftReport);
        return ShiftReportMapper.toDTO(savedReport);
    }

    @Override
    public ShiftReportDTO getShiftReportById(UUID shiftReportId) throws Exception {
        return shiftReportRepository.findById(shiftReportId)
                .map(ShiftReportMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Shift report not found with given id"));
    }

    @Override
    public List<ShiftReportDTO> getAllShiftReports() {
        List<ShiftReport> shiftReports = shiftReportRepository.findAll();
        return shiftReports.stream().map(ShiftReportMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDTO> getShiftReportsByBranchId(UUID branchId) throws Exception {
        List<ShiftReport> shiftReports = shiftReportRepository.findByBranchId(branchId);
        return shiftReports.stream().map(ShiftReportMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDTO> getShiftReportsByCashierId(UUID cashierId) throws Exception {
        List<ShiftReport> shiftReports = shiftReportRepository.findByCashierId(cashierId);
        return shiftReports.stream().map(ShiftReportMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ShiftReportDTO getCurrentShiftProgress(UUID cashierId) throws Exception {
        User cashier;

        if (cashierId != null) {
            cashier = userRepository.findById(cashierId)
                    .orElseThrow(() -> new Exception("Cashier not found"));
        } else {
            cashier = userService.getCurrentUser();
        }

        ShiftReport shiftReport = shiftReportRepository
                .findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(cashier)
                .orElseThrow(() -> new Exception("No active shift found for this cashier!"));

        LocalDateTime now = LocalDateTime.now();

        // DEBUG: Print the search criteria
        // System.out.println("DEBUG: Searching for refunds...");
        // System.out.println("DEBUG: Cashier ID: " + cashier.getId());
        // System.out.println("DEBUG: Cashier Name: " + cashier.getFullName());
        // System.out.println("DEBUG: Shift Start: " + shiftReport.getShiftStart());
        // System.out.println("DEBUG: Current Time: " + now);

        List<Order> orders = orderRepository.findByCashierAndCreatedAtBetween(
                cashier, shiftReport.getShiftStart(), now);

        // DEBUG: Show order information to help create test refunds
        // System.out.println("DEBUG: Found " + orders.size() + " orders");
        // if (!orders.isEmpty()) {
        // System.out.println("DEBUG: Sample order IDs for testing refunds:");
        // orders.stream().limit(3).forEach(order ->
        // System.out.println(" Order ID: " + order.getId() + ", Amount: " +
        // order.getTotalAmount())
        // );
        // }

        List<Refund> refunds = refundRepository.findByCashierAndCreatedAtBetween(
                cashier, shiftReport.getShiftStart(), now);

        // DEBUG: Print refund results
        // System.out.println("DEBUG: Found " + refunds.size() + " refunds");
        // if (!refunds.isEmpty()) {
        // for (Refund refund : refunds) {
        // System.out.println("DEBUG: Refund ID: " + refund.getId() +
        // ", Amount: " + refund.getAmount() +
        // ", Created: " + refund.getCreatedAt() +
        // ", Cashier: " + (refund.getCashier() != null ? refund.getCashier().getId() :
        // "NULL"));
        // }
        // }

        // Also check all refunds for this cashier without time restriction
        // Removed unused variable: List<Refund> allRefunds =
        // refundRepository.findByCashier(cashier);
        // System.out.println("DEBUG: Total refunds ever for this cashier: " +
        // allRefunds.size());

        double totalRefunds = refunds.stream()
                .mapToDouble(refund -> refund.getAmount() != null ? refund.getAmount() : 0.0)
                .sum();

        double totalSales = orders.stream()
                .mapToDouble(Order::getTotalAmount).sum();

        int totalOrders = orders.size();
        double netSale = totalSales - totalRefunds;

        // System.out.println("DEBUG: Total Sales: " + totalSales);
        // System.out.println("DEBUG: Total Refunds: " + totalRefunds);
        // System.out.println("DEBUG: Net Sale: " + netSale);

        shiftReport.setNetSale(netSale);
        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setTopSellProducts(getTopSellingProducts(orders));
        shiftReport.setPaymentSummaries(getPaymentSummaries(orders, totalSales));
        shiftReport.setRefunds(refunds);

        ShiftReport savedReport = shiftReportRepository.save(shiftReport);
        return ShiftReportMapper.toDTO(savedReport);
    }

    @Override
    public ShiftReportDTO getShiftByCashierAndDate(UUID cashierId, LocalDateTime date) throws Exception {

        User cashier = userRepository.findById(cashierId).orElseThrow(
                () -> new Exception("Cashier not found with given id"));

        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ShiftReport report = shiftReportRepository.findByCashierAndShiftStartBetween(
                cashier, start, end).orElseThrow(
                        () -> new Exception("Shift report not found on this date for " + cashier.getFullName()));

        return ShiftReportMapper.toDTO(report);
    }

    // ------------------------------Helper Methods ------------------

    private List<PaymentSummaries> getPaymentSummaries(List<Order> orders, double totalSales) {
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        Map<PaymentType, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getPaymentType() != null ? order.getPaymentType() : PaymentType.CASH));

        List<PaymentSummaries> paymentSummaries = new ArrayList<>();
        for (Map.Entry<PaymentType, List<Order>> entry : grouped.entrySet()) {
            double amount = entry.getValue().stream().mapToDouble(Order::getTotalAmount).sum();

            int transactions = entry.getValue().size();
            double percentage = totalSales > 0 ? (amount / totalSales) * 100 : 0.0;

            PaymentSummaries ps = new PaymentSummaries();
            ps.setPaymentType(entry.getKey());
            ps.setTotalAmount(amount);
            ps.setTransactionCount(transactions);
            ps.setPercentage(percentage);
            paymentSummaries.add(ps);
        }

        return paymentSummaries;
    }

    private List<ProductWithQuantity> getTopSellingProducts(List<Order> orders) {
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Product, Integer> productSalesMap = new HashMap<>();

        for (Order order : orders) {
            if (order.getItems() != null) {
                for (OrderItem orderItem : order.getItems()) {
                    Product product = orderItem.getProduct();
                    if (product != null) {
                        productSalesMap.put(product,
                                productSalesMap.getOrDefault(product, 0) + orderItem.getQuantity());
                    }
                }
            }
        }

        return productSalesMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> {
                    ProductWithQuantity pwq = new ProductWithQuantity();
                    pwq.setProduct(entry.getKey());
                    pwq.setQuantitySold(entry.getValue());
                    return pwq;
                })
                .collect(Collectors.toList());
    }

    private List<Order> getRecentOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        // Fixed: Remove the double .reversed() which cancels each other out
        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}