package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByStoreId(UUID storeId);

    @Query(
            "SELECT p FROM Product p " +
                    "WHERE p.store.id = :storeId AND (" +
                    "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "Or LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "Or LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%'))" +
                    ")"
    )
    List<Product> searchByKeyword(@Param("storeId") UUID storeId,
                                  @Param("query") String keyword);

    /**
     * Check if a product with the given SKU exists in the specified store
     *
     * @param sku The SKU to check for uniqueness
     * @param storeId The store ID to check within
     * @return true if a product with the SKU exists in the store, false otherwise
     */
    boolean existsBySkuAndStoreId(String sku, UUID storeId);
}