package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    List<User> findByStore(Store store);
    List<User> findByBranchId(UUID branchId);

}