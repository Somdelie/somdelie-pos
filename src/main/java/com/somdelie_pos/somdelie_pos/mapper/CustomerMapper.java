package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Customer;
import com.somdelie_pos.somdelie_pos.payload.dto.CustomerDTO;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        return CustomerDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .build();
    }
}
