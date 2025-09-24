package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.domain.StoreStatus;
import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.StoreDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(UUID id) throws Exception;
    List<StoreDto> getAllStores();
    Store getStoreByAdmin() throws UserException;
    StoreDto updateStore(UUID id, StoreDto storeDto) throws UserException;
    void deleteStore(UUID id) throws UserException;
    StoreDto getStoreByEmployee() throws UserException;

    StoreDto moderateStore(UUID id, StoreStatus status) throws UserException;
}