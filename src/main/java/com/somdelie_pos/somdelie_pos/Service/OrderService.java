package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.domain.OrderStatus;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO) throws Exception;
    OrderDTO getOrderById(UUID orderId) throws Exception;
    List<OrderDTO> getOrdersByBranchId(UUID branchId,
                                       UUID customerId,
                                       UUID cashierId,
                                       PaymentType paymentType,
                                       OrderStatus status) throws Exception;


    List<OrderDTO> getOrdersByCashierId(UUID cashierId) throws Exception;
    void cancelOrder(UUID orderId) throws Exception;
    void deleteOrder(UUID orderId) throws Exception;
    List<OrderDTO> getTodayOrdersByBranchId(UUID branchId) throws Exception;
    List<OrderDTO> getTodayOrdersByCashierId(UUID cashierId) throws Exception;
    List<OrderDTO> getOrdersByCustomerId(UUID customerId) throws Exception;
    List<OrderDTO> getTop5RecentOrdersByBranchId(UUID branchId) throws Exception;
}
