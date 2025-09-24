package com.somdelie_pos.somdelie_pos.repository;

import com.somdelie_pos.somdelie_pos.modal.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullName, String email);

}
