package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.payload.dto.BranchDTO;

import java.time.LocalDateTime;

public class BranchMapper {

    public static BranchDTO toDto(Branch branch){
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .email(branch.getEmail())
                .phone(branch.getPhone())
                .closingTime(branch.getClosingTime())
                .openingTime(branch.getOpeningTime())
                .workingDays(branch.getWorkingDays())
                .storeId(branch.getStore()!=null?branch.getStore().getId(): null)
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .build();
    }

    public static Branch toEntity(BranchDTO branchDTO, Store store){

        return Branch.builder()
                .name(branchDTO.getName())
                .address(branchDTO.getAddress())
                .store(store)
                .email(branchDTO.getEmail())
                .phone(branchDTO.getPhone())
                .openingTime(branchDTO.getOpeningTime())
                .closingTime(branchDTO.getClosingTime())
                .workingDays(branchDTO.getWorkingDays())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
