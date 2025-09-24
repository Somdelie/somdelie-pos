package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.CustomerService;
import com.somdelie_pos.somdelie_pos.modal.Customer;
import com.somdelie_pos.somdelie_pos.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {

        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(UUID id, Customer customer) throws Exception {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(
                () -> new Exception("Customer with id " + id + " not found")
        );

        customer.setFullName(existingCustomer.getFullName());
        customer.setEmail(existingCustomer.getEmail());
        customer.setPhone(existingCustomer.getPhone());

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) throws Exception {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(
                () -> new Exception("Customer with id " + id + " not found")
        );
        customerRepository.delete(existingCustomer);
    }

    @Override
    public Customer getCustomerById(UUID id) throws Exception {
        return customerRepository.findById(id).orElseThrow(
                () -> new Exception("Customer with id " + id + " not found")
        );
    }

    @Override
    public List<Customer> getAllCustomers() throws Exception {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) throws Exception {
        return customerRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
}
