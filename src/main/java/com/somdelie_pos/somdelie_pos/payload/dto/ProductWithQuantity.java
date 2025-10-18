package com.somdelie_pos.somdelie_pos.payload.dto;

import com.somdelie_pos.somdelie_pos.modal.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithQuantity {
    private Product product;
    private Integer quantitySold;
}
