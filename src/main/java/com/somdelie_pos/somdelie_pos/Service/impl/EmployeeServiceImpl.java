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

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new Exception("Store not found!")
        );

        Branch branch = null;

        if (employee.getRole()==UserRole.ROLE_BRANCH_MANAGER) {
            if (employee.getBranchId() == null) {
                throw new Exception("Branch id is required to create employee!");
            }

        }
            branch = branchRepository.findById(employee.getBranchId()).orElseThrow(
                    () -> new Exception("Branch not found!")
            );
            User user = UserMapper.toEntity(employee);
            user.setStore(store);
            user.setBranch(branch);
            user.setPassword(passwordEncoder.encode(employee.getPassword()));

            User savedEmployee = userRepository.save(user);
            if (employee.getRole()==UserRole.ROLE_BRANCH_MANAGER && branch != null) {
                branch.setManager(savedEmployee);
                branchRepository.save(branch);
            }

            return UserMapper.toDto(savedEmployee);

    }

    @Override
    public UserDto createBranchEmployee(UserDto employee, UUID branchId) throws Exception {

        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new Exception("Branch not found!")
        );

        if(employee.getRole()==UserRole.ROLE_BRANCH_CASHIER ||
                employee.getRole()==UserRole.ROLE_BRANCH_MANAGER) {

            User user = UserMapper.toEntity(employee);
            user.setBranch(branch);
            user.setPassword(passwordEncoder.encode(employee.getPassword()));

            return UserMapper.toDto(userRepository.save(user));
        }

        throw new Exception("Branch role is required to create employee!");
    }

    @Override
    public User updateEmployee(UUID employeeId, UserDto employeeDetails, UUID branchId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new Exception("Employee not found!")
        );

        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new Exception("Branch not found!")
        );

        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setBranch(branch);

        // only update password if provided
        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }

        return userRepository.save(existingEmployee);
    }

    @Override
    public User updateStoreEmployee(UUID employeeId, UserDto employeeDetails, UUID storeId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new Exception("Employee not found!")
        );

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new Exception("Store not found!")
        );

        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setFullName(employeeDetails.getFullName());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setStore(store);

        // only update password if provided
        if (employeeDetails.getPassword() != null && !employeeDetails.getPassword().isBlank()) {
            existingEmployee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
        }

        return userRepository.save(existingEmployee);
    }


    @Override
    public void deleteEmployee(UUID employeeId) throws Exception {

        User existingEmployee = userRepository.findById(employeeId).orElseThrow(
                () -> new Exception("Employee not found!")
        );

        userRepository.delete(existingEmployee);

    }

    @Override
    public List<UserDto> findStoreEmployees(UUID storeId, UserRole role) throws Exception {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new Exception("Store not found!")
        );

        return userRepository.findByStore(store)
                .stream().filter(user -> role==null || user.getRole()==role)
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<UserDto> findBranchEmployees(UUID branchId, UserRole role) throws Exception {
        Branch branch = branchRepository.findById(branchId).orElseThrow(
                () -> new Exception("Branch not found!")
        );

        return userRepository.findByBranchId(branchId)
                .stream().filter(
                        user -> role==null || user.getRole()==role
                )
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
