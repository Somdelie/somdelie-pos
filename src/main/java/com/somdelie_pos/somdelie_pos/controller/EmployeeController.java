package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.EmployeeService;
import com.somdelie_pos.somdelie_pos.domain.UserRole;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.UserDto;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/store/{storeId}")
    public ResponseEntity<UserDto> createStoreEmployee(
            @PathVariable UUID storeId,
            @RequestBody UserDto userDto
    ) throws Exception {
        UserDto employee = employeeService.createStoreEmployee(userDto, storeId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/branch/{branchId}")
    public ResponseEntity<UserDto> createBranchEmployee(
            @PathVariable UUID branchId,
            @RequestBody UserDto userDto
    ) throws Exception {
        UserDto employee = employeeService.createBranchEmployee(userDto, branchId);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}/store/{storeId}")
    public ResponseEntity<User> updateStoreEmployee(
            @PathVariable UUID id,
            @PathVariable UUID storeId,
            @RequestBody UserDto userDto
    ) throws Exception {
        User employee = employeeService.updateStoreEmployee(id, userDto, storeId);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}/branch/{branchId}")
    public ResponseEntity<User> updateBranchEmployee(
            @PathVariable UUID id,
            @PathVariable UUID branchId,
            @RequestBody UserDto userDto
    ) throws Exception {
        User employee = employeeService.updateEmployee(id, userDto, branchId);
        return ResponseEntity.ok(employee);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable UUID id) throws Exception {
        employeeService.deleteEmployee(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Delete employee successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDto>> getStoreEmployees(
            @PathVariable UUID storeId,
            @RequestParam(required = false) UserRole userRole
    ) throws Exception {
        List<UserDto> employees = employeeService.findStoreEmployees(storeId, userRole);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<UserDto>> getBranchEmployees(
            @PathVariable UUID branchId,
            @RequestParam(required = false) UserRole userRole
    ) throws Exception {
        List<UserDto> employees = employeeService.findBranchEmployees(branchId, userRole);
        return ResponseEntity.ok(employees);
    }
}
