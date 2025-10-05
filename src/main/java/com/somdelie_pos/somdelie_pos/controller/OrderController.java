package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.OrderService;
import com.somdelie_pos.somdelie_pos.domain.OrderStatus;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO order) throws Exception {

        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByBranchId(
            @PathVariable UUID branchId,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID cashierId,
            @RequestParam(required = false)PaymentType paymentType,
            @RequestParam(required = false)OrderStatus orderStatus
            )  throws Exception {
        return ResponseEntity.ok(orderService.getOrdersByBranchId(branchId,
                customerId,cashierId,paymentType, orderStatus));
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCashierId(
            @PathVariable UUID cashierId) throws Exception {
        return ResponseEntity.ok(orderService.getOrdersByCashierId(cashierId));
    }

    @GetMapping("/today/branch/{branchId}")
    public ResponseEntity<List<OrderDTO>> getTodayOrdersByBranchId(
            @PathVariable UUID branchId) throws Exception {
        return ResponseEntity.ok(orderService.getTodayOrdersByBranchId(branchId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(
            @PathVariable UUID customerId) throws Exception {
        return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/recent/{branchId}")
    public ResponseEntity<List<OrderDTO>> getRecentOrdersByBranchId(
            @PathVariable UUID branchId
    )  throws Exception {
        return ResponseEntity.ok(orderService.getTop5RecentOrdersByBranchId(branchId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deleteOrder(
            @PathVariable UUID orderId
    )  throws Exception {
        orderService.deleteOrder(orderId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Order has been deleted");
        return ResponseEntity.ok(apiResponse);
    }

}
