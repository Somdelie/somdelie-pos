package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.ProductService;
import com.somdelie_pos.somdelie_pos.exceptions.ResourceAlreadyExistsException;
import com.somdelie_pos.somdelie_pos.mapper.ProductMapper;
import com.somdelie_pos.somdelie_pos.modal.Category;
import com.somdelie_pos.somdelie_pos.modal.Product;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;
import com.somdelie_pos.somdelie_pos.repository.CategoryRepository;
import com.somdelie_pos.somdelie_pos.repository.ProductRepository;
import com.somdelie_pos.somdelie_pos.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, User user) throws Exception {
        Store store = storeRepository.findById(productDTO.getStoreId())
                .orElseThrow(() -> new Exception("Store not found"));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new Exception("Category not found!"));

        // Check if product with SKU already exists in the store
        if (productRepository.existsBySkuAndStoreId(productDTO.getSku(), productDTO.getStoreId())) {
            throw new ResourceAlreadyExistsException("Product", "sku", productDTO.getSku(),
                    "this store", productDTO.getStoreId());
        }

        Product product = ProductMapper.toEntity(productDTO, store, category);
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(UUID id, ProductDTO productDTO, User user) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));

        // Check if SKU is being changed and if the new SKU already exists in the store
        if (!product.getSku().equals(productDTO.getSku())) {
            if (productRepository.existsBySkuAndStoreId(productDTO.getSku(), product.getStore().getId())) {
                throw new ResourceAlreadyExistsException("Product", "sku", productDTO.getSku(),
                        "store", product.getStore().getId());
            }
        }

        product.setName(productDTO.getName());
        product.setSku(productDTO.getSku());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setBrand(productDTO.getBrand());
        product.setMrp(productDTO.getMrp());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setUpdatedDate(LocalDateTime.now());

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new Exception("Category not found!"));
            product.setCategory(category);
        }

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public void deleteProduct(UUID id, User user) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product does not exists!"));

        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getProductsByStoreId(UUID storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);
        return products.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchByKeyword(UUID storeId, String keyword) {
        List<Product> products = productRepository.searchByKeyword(storeId, keyword);
        return products.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }
}