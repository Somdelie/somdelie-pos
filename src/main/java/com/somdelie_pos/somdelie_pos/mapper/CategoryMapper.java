package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Category;
import com.somdelie_pos.somdelie_pos.payload.dto.CategoryDTO;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(category.getStore() != null ? category.getStore().getId() : null)
                .build();
    }
}