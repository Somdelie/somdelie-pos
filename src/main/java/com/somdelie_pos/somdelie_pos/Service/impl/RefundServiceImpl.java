package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.RefundService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.mapper.RefundMapper;
import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Order;
import com.somdelie_pos.somdelie_pos.modal.Refund;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.RefundDTO;
import com.somdelie_pos.somdelie_pos.repository.OrderRepository;
import com.somdelie_pos.somdelie_pos.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final UserService userService;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;

    @Override
    public RefundDTO createRefund(RefundDTO refund) throws Exception {

        User cashier = userService.getCurrentUser();

        Order order = orderRepository.findById(refund.getOrderId())
                .orElseThrow(() -> new Exception("Order not found"));

        Branch branch = order.getBranch();

        Refund createdRefund = Refund.builder()
                .cashier(cashier)
                .branch(branch)
                .order(order)
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        Refund savedRefund = refundRepository.save(createdRefund);

        return RefundMapper.toDTO(savedRefund);
    }

    @Override
    public List<RefundDTO> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RefundDTO getRefundById(UUID refundId) throws Exception {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new Exception("Refund not found"));
        return RefundMapper.toDTO(refund);
    }

    @Override
    public List<RefundDTO> getRefundByCashierId(UUID cashierId) throws Exception {
        User cashier = userService.getUserById(cashierId);
        return refundRepository.findByCashier(cashier).stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getRefundByShiftReportId(UUID shiftReportId) {
        return refundRepository.findByShiftReportId(shiftReportId).stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getRefundsByCashierAndDateRange(UUID cashierId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        User cashier = userService.getUserById(cashierId);
        return refundRepository.findByCashierAndCreatedAtBetween(cashier, startDate, endDate).stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getRefundByBranchId(UUID branchId) {
        return refundRepository.findByBranchId(branchId).stream()
                .map(RefundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRefund(UUID refundId) throws Exception {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new Exception("Refund not found!"));
        refundRepository.delete(refund);
    }
}
