package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.OrderService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.domain.OrderStatus;
import com.somdelie_pos.somdelie_pos.domain.PaymentType;
import com.somdelie_pos.somdelie_pos.mapper.OrderMapper;
import com.somdelie_pos.somdelie_pos.modal.*;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;
import com.somdelie_pos.somdelie_pos.repository.CustomerRepository;
import com.somdelie_pos.somdelie_pos.repository.OrderItemRepository;
import com.somdelie_pos.somdelie_pos.repository.OrderRepository;
import com.somdelie_pos.somdelie_pos.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final UserService userService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository; // inject here

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {

        // Get the current cashier
        User cashier = userService.getCurrentUser();
        Branch branch = cashier.getBranch();

        if (branch == null) {
            throw new Exception("Branch is required!");
        }

        // Handle customer: existing or new
        Customer customer = null;
        if (orderDTO.getCustomer() != null) {
            if (orderDTO.getCustomer().getId() != null) {
                // Existing customer: fetch from DB
                customer = customerRepository.findById(orderDTO.getCustomer().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
            } else {
                // New customer: map DTO → entity, then save
                Customer newCustomer = Customer.builder()
                        .fullName(orderDTO.getCustomer().getFullName())
                        .email(orderDTO.getCustomer().getEmail())
                        .phone(orderDTO.getCustomer().getPhone())
                        .address(orderDTO.getCustomer().getAddress())
                        .build();
                customer = customerRepository.save(newCustomer);
            }
        }

        // Create the order
        Order order = Order.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(customer)
                .paymentType(orderDTO.getPaymentType())
                .status(orderDTO.getStatus() != null ? orderDTO.getStatus() : OrderStatus.COMPLETED) // ✅ Add this
                .build();

        // Map DTO items → entity items
        List<OrderItem> orderItems = orderDTO.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

            double itemPrice = product.getSellingPrice() * itemDto.getQuantity();

            return OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .price(itemPrice)
                    .order(order) // link back to parent order
                    .build();
        }).toList();

        // Set total and items
        double total = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalAmount(total);
        order.setItems(orderItems);

        // Save order (cascades items because of cascade = ALL)
        Order savedOrder = orderRepository.save(order);

        // Return mapped DTO
        return OrderMapper.toDTO(savedOrder);
    }



    @Override
    public OrderDTO getOrderById(UUID orderId) throws Exception {

        return orderRepository.findById(orderId)
                .map(OrderMapper::toDTO)
                .orElseThrow(
                () -> new Exception("Order not found with id "+ orderId)
        );
    }

    @Override
    public List<OrderDTO> getOrdersByBranchId(UUID branchId,
                                              UUID customerId,
                                              UUID cashierId,
                                              PaymentType paymentType,
                                              OrderStatus status) throws Exception {
        return orderRepository.findByBranchId(branchId).stream()
                .filter(order -> customerId==null ||
                        (order.getCustomer()!=null &&
                                order.getCustomer().getId().equals(customerId)))
                .filter(order -> cashierId==null ||
                        order.getCashier()!=null &&
                        order.getCashier().getId().equals(cashierId))
                .filter(order -> paymentType==null ||
                        order.getPaymentType()==paymentType)
                .map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByCashierId(UUID cashierId) throws Exception {
        return orderRepository.findByCashierId(cashierId).stream()
                .map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(UUID orderId) throws Exception {
    }

    @Override
    public void deleteOrder(UUID orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id "+ orderId)
        );

        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getTodayOrdersByBranchId(UUID branchId) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return orderRepository.findByBranchIdAndCreatedAtBetween(
                branchId, start, end
        ).stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getTodayOrdersByCashierId(UUID cashierId) throws Exception {

        return List.of();
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(UUID customerId) throws Exception {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getTop5RecentOrdersByBranchId(UUID branchId) throws Exception {
        return orderRepository.findTop5ByBranchIdOrderByCreatedAtDesc(branchId)
                .stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }
}
