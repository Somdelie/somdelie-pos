package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Category;
import com.somdelie_pos.somdelie_pos.modal.Product;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .mrp(product.getMrp())
                .sellingPrice(product.getSellingPrice())
                .brand(product.getBrand())
                .image(product.getImage())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .storeId(product.getStore() != null ? product.getStore().getId() : null)
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                // quantitySold will be set by caller when needed (e.g., in ShiftReportMapper)
                .quantitySold(null)
                .build();
    }

    public static Product toEntity(ProductDTO productDTO, Store store, Category category) {
        return Product.builder()
                .name(productDTO.getName())
                .store(store)
                .category(category)
                .sku(productDTO.getSku())
                .description(productDTO.getDescription())
                .mrp(productDTO.getMrp())
                .sellingPrice(productDTO.getSellingPrice())
                .brand(productDTO.getBrand())
                .image(productDTO.getImage())
                .build();
    }
}