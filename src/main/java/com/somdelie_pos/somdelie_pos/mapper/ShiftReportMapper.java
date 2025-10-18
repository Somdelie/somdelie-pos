package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.modal.Refund;
import com.somdelie_pos.somdelie_pos.modal.ShiftReport;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductWithQuantity;
import com.somdelie_pos.somdelie_pos.payload.dto.RefundDTO;
import com.somdelie_pos.somdelie_pos.payload.dto.ShiftReportDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {

    public static ShiftReportDTO toDTO(ShiftReport shiftReport) {
        return ShiftReportDTO.builder()
                .id(shiftReport.getId())
                .shiftStart(shiftReport.getShiftStart() != null ? shiftReport.getShiftStart().toLocalTime() : null)
                .shiftEnd(shiftReport.getShiftEnd() != null ? shiftReport.getShiftEnd().toLocalTime() : null)
                .totalSales(shiftReport.getTotalSales())
                .totalRefunds(shiftReport.getTotalRefunds())
                .netSale(shiftReport.getNetSale())
                .totalOrders(shiftReport.getTotalOrders())
                .cashier(shiftReport.getCashier() != null ? UserMapper.toDto(shiftReport.getCashier()) : null)
                .cashierId(shiftReport.getCashier() != null ? shiftReport.getCashier().getId() : null)
                .branchId(shiftReport.getBranch() != null ? shiftReport.getBranch().getId() : null)
                .recentOrders(mapOrders(shiftReport.getRecentOrders()))
                .topSellProducts(mapProductsWithQuantity(shiftReport.getTopSellProducts()))
                .refunds(mapRefunds(shiftReport.getRefunds()))
                .paymentSummaries(shiftReport.getPaymentSummaries())
                .build();
    }

    private static List<RefundDTO> mapRefunds(List<Refund> refunds) {
        if (refunds == null || refunds.isEmpty()) {
            return new ArrayList<>();
        }
        return refunds.stream().map(RefundMapper::toDTO).collect(Collectors.toList());
    }

    // FIXED: Handle ProductWithQuantity wrapper objects
    private static List<ProductDTO> mapProductsWithQuantity(List<ProductWithQuantity> productsWithQuantity) {
        if (productsWithQuantity == null || productsWithQuantity.isEmpty()) {
            return new ArrayList<>();
        }
        return productsWithQuantity.stream()
                .map(pwq -> {
                    // Convert the product to DTO
                    ProductDTO dto = ProductMapper.toDTO(pwq.getProduct());
                    // Set the quantitySold from the wrapper
                    dto.setQuantitySold(pwq.getQuantitySold());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static List<OrderDTO> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null || recentOrders.isEmpty()) {
            return new ArrayList<>();
        }
        return recentOrders.stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }
}