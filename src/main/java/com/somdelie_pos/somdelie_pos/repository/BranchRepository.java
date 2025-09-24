package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    // Since Store ID is now UUID, this should be UUID
    List<Branch> findByStoreId(UUID storeId);
}
