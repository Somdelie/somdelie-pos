package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.CustomerService;
import com.somdelie_pos.somdelie_pos.modal.Customer;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable UUID id,
            @RequestBody Customer customer) throws Exception {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(
            @PathVariable UUID id) throws Exception {
        customerService.deleteCustomer(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Delete customer successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() throws Exception {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Customer>> searchCustomer(
            @RequestParam String keyword
    ) throws Exception {
        return ResponseEntity.ok(customerService.searchCustomers(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

}
