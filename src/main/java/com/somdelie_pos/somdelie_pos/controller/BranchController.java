package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.BranchService;
import com.somdelie_pos.somdelie_pos.payload.dto.BranchDTO;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import com.somdelie_pos.somdelie_pos.payload.dto.BranchScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) throws Exception {

        BranchDTO createdBranch = branchService.createBranch(branchDTO);
        return ResponseEntity.ok(createdBranch);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(
            @PathVariable UUID id) throws Exception {
        BranchDTO branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<BranchDTO>> getAllBranchesByStoreId(
            @PathVariable UUID storeId) throws Exception {
        List<BranchDTO> branches = branchService.getAllBranchesByStoreId(storeId);
        return ResponseEntity.ok(branches);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable UUID id,
            @RequestBody BranchDTO branchDTO) throws Exception {

        BranchDTO updatedBranch = branchService.updateBranch(id, branchDTO);
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBranchById(
            @PathVariable UUID id) throws Exception {
        branchService.deleteBranch(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Successfully deleted branch");
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}/schedule")
    public ResponseEntity<BranchDTO> updateSchedule(
            @PathVariable UUID id,
            @RequestBody BranchScheduleRequest request) throws Exception {
        BranchDTO updated = branchService.updateSchedule(
                id,
                request.getWorkingDays(),
                request.getOpeningTime(),
                request.getClosingTime());
        return ResponseEntity.ok(updated);
    }
}
