package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    Store findByStoreAdmin(User admin);
}

