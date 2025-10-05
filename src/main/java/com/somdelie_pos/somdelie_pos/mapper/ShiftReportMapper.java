package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.modal.Product;
import com.somdelie_pos.somdelie_pos.modal.Refund;
import com.somdelie_pos.somdelie_pos.modal.ShiftReport;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.RefundDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.ShiftReportDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {

    public static ShiftReportDTO toDTO(ShiftReport shiftReport) {
        return ShiftReportDTO.builder()
                .id(shiftReport.getId())
                // Fixed: Properly handle null values and convert LocalDateTime to LocalTime
                .shiftStart(shiftReport.getShiftStart() != null ?
                        shiftReport.getShiftStart().toLocalTime() : null)
                .shiftEnd(shiftReport.getShiftEnd() != null ?
                        shiftReport.getShiftEnd().toLocalTime() : null)
                .totalSales(shiftReport.getTotalSales())
                .totalRefunds(shiftReport.getTotalRefunds())
                .netSale(shiftReport.getNetSale())
                .totalOrders(shiftReport.getTotalOrders())
                .cashier(shiftReport.getCashier() != null ?
                        UserMapper.toDto(shiftReport.getCashier()) : null)
                .cashierId(shiftReport.getCashier() != null ?
                        shiftReport.getCashier().getId() : null)
                .branchId(shiftReport.getBranch() != null ?
                        shiftReport.getBranch().getId() : null)
                .recentOrders(mapOrders(shiftReport.getRecentOrders()))
                .topSellProducts(mapProducts(shiftReport.getTopSellProducts()))
                .refunds(mapRefunds(shiftReport.getRefunds()))
                .paymentSummaries(shiftReport.getPaymentSummaries())
                .build();
    }

    private static List<RefundDTO> mapRefunds(List<Refund> refunds) {
        if (refunds == null || refunds.isEmpty()) {
            return new ArrayList<>();  // Return empty list instead of null
        }
        return refunds.stream().map(RefundMapper::toDTO).collect(Collectors.toList());
    }

    private static List<ProductDTO> mapProducts(List<Product> topSellProducts) {
        if (topSellProducts == null || topSellProducts.isEmpty()) {
            return new ArrayList<>();  // Return empty list instead of null
        }
        return topSellProducts.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    private static List<OrderDTO> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null || recentOrders.isEmpty()) {
            return new ArrayList<>();  // Return empty list instead of null
        }
        return recentOrders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }
}