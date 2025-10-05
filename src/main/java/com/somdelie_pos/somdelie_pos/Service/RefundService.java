package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.payload.dto.RefundDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RefundService {

    RefundDTO createRefund(RefundDTO refund) throws Exception;
    List<RefundDTO> getAllRefunds() throws Exception;
    RefundDTO getRefundById(UUID refundId) throws Exception;
    List<RefundDTO> getRefundByCashierId(UUID cashierId) throws Exception;
    List<RefundDTO> getRefundByShiftReportId(UUID shiftReportId) throws Exception;
    List<RefundDTO> getRefundsByCashierAndDateRange(UUID cashierId,
                                                    LocalDateTime startDate,
                                                    LocalDateTime endDate) throws Exception;
    List<RefundDTO> getRefundByBranchId(UUID branchId) throws Exception;

    void deleteRefund(UUID refundId) throws Exception;
}
