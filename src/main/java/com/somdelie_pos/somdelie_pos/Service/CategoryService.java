package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.payload.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO dto) throws Exception;
    List<CategoryDTO> getAllCategoriesByStore(UUID storeId);
    CategoryDTO getCategoryById(UUID id);
    CategoryDTO updateCategory(UUID id, CategoryDTO dto) throws Exception;
    void deleteCategory(UUID id) throws Exception;
}