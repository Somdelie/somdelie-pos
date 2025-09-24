package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByProductIdAndBranchId(UUID productId, UUID branchId);

    List<Inventory> findByBranchId(UUID branchId);

//    List<Inventory> findByProductId(UUID productId);
}
