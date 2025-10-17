package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.BranchService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.mapper.BranchMapper;
import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.BranchDTO;
import com.somdelie_pos.somdelie_pos.repository.BranchRepository;
import com.somdelie_pos.somdelie_pos.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;;
    private final UserService userService;

    @Override
    public BranchDTO createBranch(BranchDTO branchDTO) throws UserException {
        User currentUser = userService.getCurrentUser();
        Store store = storeRepository.findByStoreAdmin(currentUser);

        Branch branch = BranchMapper.toEntity(branchDTO, store);
        Branch savedBranch = branchRepository.save(branch);
        return BranchMapper.toDto(savedBranch);
    }

    @Override
    public BranchDTO updateBranch(UUID id, BranchDTO branchDTO) throws Exception {
        Branch existing = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch does not exist..."));

        existing.setName(branchDTO.getName());
        existing.setWorkingDays(branchDTO.getWorkingDays());
        existing.setEmail(branchDTO.getEmail());
        existing.setAddress(branchDTO.getAddress());
        existing.setPhone(branchDTO.getPhone());
        existing.setEmail(branchDTO.getEmail());
        existing.setOpeningTime(branchDTO.getOpeningTime());
        existing.setClosingTime(branchDTO.getClosingTime());
        existing.setUpdatedAt(LocalDateTime.now());

        Branch updatedBranch = branchRepository.save(existing);
        return BranchMapper.toDto(updatedBranch);
    }

    @Override
    public void deleteBranch(UUID id) throws Exception {
        Branch existing = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch does not exists..."));

        branchRepository.delete(existing);

    }

    @Override
    public List<BranchDTO> getAllBranchesByStoreId(UUID storeId) {

        List<Branch> branches = branchRepository.findByStoreId(storeId);
        return branches.stream().map(BranchMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public BranchDTO getBranchById(UUID id) throws Exception {
        Branch existing = branchRepository.findById(id).orElseThrow(
                () -> new Exception("Branch does not exists..."));
        return BranchMapper.toDto(existing);
    }

    @Override
    public BranchDTO updateSchedule(UUID branchId, List<String> workingDays, java.time.LocalTime openingTime,
            java.time.LocalTime closingTime) throws Exception {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch does not exist..."));

        if (openingTime == null || closingTime == null) {
            throw new Exception("Opening and closing times are required");
        }
        if (!closingTime.isAfter(openingTime)) {
            throw new Exception("Closing time must be after opening time");
        }

        // Validate days; allow any subset of MONDAY..SUNDAY (weekends included)
        if (workingDays != null) {
            java.util.Set<String> allowed = java.util.EnumSet.allOf(java.time.DayOfWeek.class)
                    .stream().map(Enum::name).collect(java.util.stream.Collectors.toSet());

            for (String day : workingDays) {
                if (day == null)
                    continue;
                String normalized = day.trim().toUpperCase();
                if (!allowed.contains(normalized)) {
                    throw new Exception("Invalid day: " + day + ". Allowed values are MONDAY..SUNDAY");
                }
            }

            // normalize to uppercase
            workingDays = workingDays.stream().filter(java.util.Objects::nonNull)
                    .map(d -> d.trim().toUpperCase()).toList();
            branch.setWorkingDays(workingDays);
        }

        branch.setOpeningTime(openingTime);
        branch.setClosingTime(closingTime);
        branch.setUpdatedAt(java.time.LocalDateTime.now());

        Branch saved = branchRepository.save(branch);
        return BranchMapper.toDto(saved);
    }
}
