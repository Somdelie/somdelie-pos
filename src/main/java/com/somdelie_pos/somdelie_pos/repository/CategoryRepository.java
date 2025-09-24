package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByStoreId(UUID storeId);

    /**
     * Check if a category with the given name exists in the specified store
     *
     * @param name The category name to check for uniqueness
     * @param storeId The store ID to check within
     * @return true if a category with the name exists in the store, false otherwise
     */
    boolean existsByNameAndStoreId(String name, UUID storeId);
}