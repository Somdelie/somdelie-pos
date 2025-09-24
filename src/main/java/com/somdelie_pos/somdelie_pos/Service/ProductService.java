package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO, User user) throws Exception;
    ProductDTO updateProduct(UUID id, ProductDTO productDTO, User user) throws Exception;
    void deleteProduct(UUID id, User user) throws Exception;
    List<ProductDTO> getProductsByStoreId(UUID storeId);
    List<ProductDTO> searchByKeyword(UUID id, String keyword);
}
