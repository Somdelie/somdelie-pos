package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.CategoryService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.domain.UserRole;
import com.somdelie_pos.somdelie_pos.exceptions.ResourceAlreadyExistsException;
import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.mapper.CategoryMapper;
import com.somdelie_pos.somdelie_pos.modal.Category;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.CategoryDTO;
import com.somdelie_pos.somdelie_pos.repository.CategoryRepository;
import com.somdelie_pos.somdelie_pos.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final StoreRepository storeRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) throws Exception {
        User user = userService.getCurrentUser();

        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(
                () -> new Exception("Store not found")
        );

        // Check if category with name already exists in the store
        if (categoryRepository.existsByNameAndStoreId(dto.getName(), dto.getStoreId())) {
            throw new ResourceAlreadyExistsException("Category", "name", dto.getName(),
                    "store", dto.getStoreId());
        }

        Category category = Category.builder()
                .store(store)
                .name(dto.getName())
                .build();

        checkAuthority(user, category.getStore());

        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDTO> getAllCategoriesByStore(UUID storeId) {
        List<Category> categories = categoryRepository.findByStoreId(storeId);
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category != null ? CategoryMapper.toDTO(category) : null;
    }

    @Override
    public CategoryDTO updateCategory(UUID id, CategoryDTO dto) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new Exception("Category not found!")
        );
        User user = userService.getCurrentUser();

        // Check if name is being changed and if the new name already exists in the store
        if (!category.getName().equals(dto.getName())) {
            if (categoryRepository.existsByNameAndStoreId(dto.getName(), category.getStore().getId())) {
                throw new ResourceAlreadyExistsException("Category", "name", dto.getName(),
                        "store", category.getStore().getId());
            }
        }

        category.setName(dto.getName());
        checkAuthority(user, category.getStore());
        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new Exception("Category not found!")
        );

        User user = userService.getCurrentUser();

        checkAuthority(user, category.getStore());

        categoryRepository.delete(category);
    }

    private void checkAuthority(User user, Store store) throws Exception {
        if (user == null) {
            throw new UserException("Please login to continue!");
        }

        boolean isAdmin = user.getRole().equals(UserRole.ROLE_STORE_ADMIN);
        boolean isManager = user.getRole().equals(UserRole.ROLE_STORE_MANAGER);
        boolean isSameStore = user.equals(store.getStoreAdmin());

        if (!(isAdmin && isSameStore) && !isManager) {
            throw new Exception("Oops! you don't have permission to manage this category");
        }
    }
}