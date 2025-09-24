package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.domain.UserRole;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    UserDto createStoreEmployee(UserDto employee, UUID storeId) throws Exception;
    UserDto createBranchEmployee(UserDto employee, UUID branchId) throws Exception;

    User updateEmployee(UUID employeeId, UserDto employeeDetails, UUID branchId) throws Exception;
    User updateStoreEmployee(UUID employeeId, UserDto employeeDetails, UUID storeId) throws Exception;

    void deleteEmployee(UUID employeeId) throws Exception;
    List<UserDto> findStoreEmployees(UUID storeId, UserRole role) throws Exception;
    List<UserDto> findBranchEmployees(UUID branchId, UserRole role) throws Exception;
}

