package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.Service.EmployeeService;
import com.somdelie_pos.somdelie_pos.domain.UserRole;
import com.somdelie_pos.somdelie_pos.mapper.UserMapper;
import com.somdelie_pos.somdelie_pos.modal.Branch;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;
import com.somdelie_pos.somdelie_pos.repository.BranchRepository;
import com.somdelie_pos.somdelie_pos.repository.StoreRepository;
import com.somdelie_pos.somdelie_pos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(UserDto employee, UUID storeId) throws Exception {

        // Check if email already exists
        if (userRepository.existsByEmail(employee.getEmail())) {
            throw new Exception("User with email '" + employee.getEmail() + "' already exists!");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Store not found"));

        Branch branch = null;

        // Only require branchId for ROLE_BRANCH_CASHIER
        if (employee.getRole() == UserRole.ROLE_BRANCH_CASHIER) {
            if (employee.getBranchId() == null) {
                throw new Exception("Branch id is required for ROLE_BRANCH_CASHIER!");
            }
            branch = branchRepository.findById(employee.getBranchId()).orElseThrow(
                    () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Branch not found"));
        }
        // Optional branchId for ROLE_BRANCH_MANAGER
        else if (employee.getRole() == UserRole.ROLE_BRANCH_MANAGER && employee.getBranchId() != null) {
            branch = branchRepository.findById(employee.getBranchId()).orElseThrow(
                    () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Branch not found"));
        }

        User user = UserMapper.toEntity(employee);
        user.setStore(store);
        user.setBranch(branch);
        user.setPassword(passwordEncoder.encode(employee.getPassword()));

        try {
            User savedEmployee = userRepository.save(user);

            // Set branch manager if applicable
            if (employee.getRole() == UserRole.ROLE_BRANCH_MANAGER && branch != null) {
                branch.setManager(savedEmployee);
                branchRepository.save(branch);
            }

            return UserMapper.toDto(savedEmployee);
        } catch (DataIntegrityViolationException e) {
            // Let GlobalExceptionHandler format this nicely
            throw e;
        }

    }

    @Override
    public UserDto createBranchEmployee(UserDto employee, UUID branchId) throws Exception {

        // Check if email already exists
        if (userRepository.existsByEmail(employee.getEmail())) {
            throw new Exception("User with email '" + employee.getEmail() + "' already exists!");
        }

        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new Exception("Branch not found!"));

        if (employee.getRole() == UserRole.ROLE_BRANCH_CASHIER ||
                employee.getRole() == UserRole.ROLE_BRANCH_MANAGER) {

            User user = UserMapper.toEntity(employee);
            user.setBranch(branch);
            user.setPassword(passwordEncoder.encode(employee.getPassword()));

            try {
                return UserMapper.toDto(userRepository.save(user));
            } catch (DataIntegrityViolationException e) {
                throw e;
            }
        }

        throw new Exception("Branch role is required to create employee!");
    }

    @Override
    public User updateEmployee(UUID employeeId, UserDto employeeDetails, UUID branchId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Employee not found"));

        // Check if email already exists for a different user
        User userWithEmail = userRepository.findByEmail(employeeDetails.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(employeeId)) {
            throw new Exception("User with email '" + employeeDetails.getEmail() + "' already exists!");
        }

        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Branch not found"));

        if (employeeDetails.getEmail() == null || employeeDetails.getEmail().isBlank()) {
            throw new Exception("Email is required");
        }
        if (employeeDetails.getFullName() == null || employeeDetails.getFullName().isBlank()) {
            throw new Exception("Full name is required");
        }
        if (employeeDetails.getRole() == null) {
            throw new Exception("Role is required");
        }
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setBranch(branch);

        // only update password if provided
        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }

        try {
            return userRepository.save(existingEmployee);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public User updateStoreEmployee(UUID employeeId, UserDto employeeDetails, UUID storeId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Employee not found"));

        // Check if email already exists for a different user
        User userWithEmail = userRepository.findByEmail(employeeDetails.getEmail());
        if (userWithEmail != null && !userWithEmail.getId().equals(employeeId)) {
            throw new Exception("User with email '" + employeeDetails.getEmail() + "' already exists!");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Store not found"));

        if (employeeDetails.getEmail() == null || employeeDetails.getEmail().isBlank()) {
            throw new Exception("Email is required");
        }
        if (employeeDetails.getFullName() == null || employeeDetails.getFullName().isBlank()) {
            throw new Exception("Full name is required");
        }
        if (employeeDetails.getRole() == null) {
            throw new Exception("Role is required");
        }
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setStore(store);

        // only update password if provided
        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }

        try {
            return userRepository.save(existingEmployee);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    @Override
    public void deleteEmployee(UUID employeeId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Employee not found"));

        userRepository.delete(existingEmployee);

    }

    @Override
    public List<UserDto> findStoreEmployees(UUID storeId, UserRole role) throws Exception {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Store not found"));

        return userRepository.findByStore(store)
                .stream().filter(user -> role == null || user.getRole() == role)
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<UserDto> findBranchEmployees(UUID branchId, UserRole role) throws Exception {
        branchRepository.findById(branchId).orElseThrow(
                () -> new com.somdelie_pos.somdelie_pos.exception.ResourceNotFoundException("Branch not found"));

        return userRepository.findByBranchId(branchId)
                .stream().filter(
                        user -> role == null || user.getRole() == role)
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
