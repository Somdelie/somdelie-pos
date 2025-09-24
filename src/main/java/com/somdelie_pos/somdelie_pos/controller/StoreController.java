package com.somdelie_pos.somdelie_pos.controller;

import com.somdelie_pos.somdelie_pos.Service.StoreService;
import com.somdelie_pos.somdelie_pos.Service.UserService;
import com.somdelie_pos.somdelie_pos.domain.StoreStatus;
import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.mapper.StoreMapper;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.StoreDto;
import com.somdelie_pos.somdelie_pos.payload.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto,
                                                @RequestHeader("Authorization")String jwt) throws UserException {

        User user = userService.getUserFromJwtToken(jwt);

        return ResponseEntity.ok(storeService.createStore(storeDto,user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable UUID id,
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestHeader("Authorization") String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoreByAdmin(
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(StoreMapper.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoreByEmployee(
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    @PutMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable UUID id,
            @RequestParam StoreStatus  status
    ) throws Exception {

        return ResponseEntity.ok(storeService.moderateStore(id, status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(
            @PathVariable UUID id,
            @RequestBody StoreDto storeDto
    ) throws Exception {

        return ResponseEntity.ok(storeService.updateStore(id, storeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStore(
            @PathVariable UUID id
    ) throws Exception {

        storeService.deleteStore(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Store successfully deleted");

        return ResponseEntity.ok(apiResponse);
    }
}