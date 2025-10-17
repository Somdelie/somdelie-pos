package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.payload.dto.BranchDTO;

import java.util.List;
import java.util.UUID;

public interface BranchService {

    BranchDTO createBranch(BranchDTO branchDTO) throws UserException;

    BranchDTO updateBranch(UUID id, BranchDTO branchDTO) throws Exception;

    void deleteBranch(UUID id) throws Exception;

    List<BranchDTO> getAllBranchesByStoreId(UUID storeId);

    BranchDTO getBranchById(UUID id) throws Exception;

    /**
     * Update the schedule (working days + opening/closing times) for a branch.
     * This should accept any subset of MONDAY..SUNDAY, including weekends.
     */
    BranchDTO updateSchedule(UUID branchId, List<String> workingDays, java.time.LocalTime openingTime,
            java.time.LocalTime closingTime) throws Exception;
}
