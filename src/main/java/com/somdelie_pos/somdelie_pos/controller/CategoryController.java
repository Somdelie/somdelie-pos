package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.CategoryService;
import com.somdelie_pos.somdelie_pos.payload.dto.CategoryDTO;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO) throws Exception {

        return ResponseEntity.ok(
                categoryService.createCategory(categoryDTO));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDTO>> getByStoreId(
            @PathVariable UUID storeId) {
        return ResponseEntity.ok(
                categoryService.getAllCategoriesByStore(storeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable UUID id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryDTO categoryDTO) throws Exception {

        return ResponseEntity.ok(
                categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable UUID id) throws Exception {

        categoryService.deleteCategory(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Category successfully deleted");

        return ResponseEntity.ok(apiResponse);
    }
}