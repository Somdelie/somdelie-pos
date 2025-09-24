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
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)  // <-- use ID
                .storeId(product.getStore() != null ? product.getStore().getId() : null)
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .build();

    }

    public static Product toEntity(ProductDTO productDTO, Store store, Category category) {
        return Product.builder()
                .name(productDTO.getName())
                .store(store)
                .category(category) // still assign full entity in service
                .sku(productDTO.getSku())
                .description(productDTO.getDescription())
                .mrp(productDTO.getMrp())
                .sellingPrice(productDTO.getSellingPrice())
                .brand(productDTO.getBrand())
                .image(productDTO.getImage())
                .build();
    }
}
