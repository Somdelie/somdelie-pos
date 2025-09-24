package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.modal.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(Customer customer);
    Customer updateCustomer(UUID id, Customer customer) throws Exception;
    void deleteCustomer(UUID id) throws Exception;
    Customer getCustomerById(UUID id) throws Exception;
    List<Customer> getAllCustomers() throws Exception;
    List<Customer> searchCustomers(String keyword) throws Exception;
}
