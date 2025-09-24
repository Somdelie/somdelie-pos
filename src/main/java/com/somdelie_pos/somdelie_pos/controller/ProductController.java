package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.ProductService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.ProductDTO;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO,
                                                    @RequestHeader("Authorization")String jwt) throws Exception {

        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(
                productService.createProduct(
                        productDTO, user

                )
        );

    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDTO>> getByStoreId(
            @PathVariable UUID storeId,
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(
                productService.getProductsByStoreId(
                       storeId

                )
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID id,
            @RequestBody ProductDTO productDTO,
            @RequestHeader("Authorization")String jwt) throws Exception{

        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(
                productService.updateProduct(
                        id,
                        productDTO,
                        user
                )
        );
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDTO>> searchByKeyword(
            @PathVariable UUID storeId,
            @RequestParam String keyword,
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(
                productService.searchByKeyword(
                        storeId,
                        keyword

                )
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(
            @PathVariable UUID id,
            @RequestHeader("Authorization")String jwt) throws Exception{

        User user = userService.getUserFromJwtToken(jwt);

        productService.deleteProduct(id, user);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product successfully deleted");

        return ResponseEntity.ok(
               apiResponse
        );
    }
}
