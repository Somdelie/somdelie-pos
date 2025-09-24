package com.somdelie_pos.somdelie_pos.mapper;

import com.somdelie_pos.somdelie_pos.modal.Store;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.StoreDto;

public class StoreMapper {

    public static StoreDto toDto(Store store) {
        if (store == null) {
            return null;
        }

        StoreDto storeDto = new StoreDto();
        storeDto.setId(store.getId()); // UUID to UUID
        storeDto.setBrandName(store.getBrandName());
        storeDto.setDescription(store.getDescription());
        storeDto.setStoreAdmin(UserMapper.toDto(store.getStoreAdmin()));
        storeDto.setStoreType(store.getStoreType());
        storeDto.setContact(store.getContact());
        storeDto.setCreatedDate(store.getCreatedDate());
        storeDto.setUpdatedDate(store.getUpdatedDate());
        storeDto.setStatus(store.getStatus());

        return storeDto;
    }

    public static Store toEntity(StoreDto storeDto, User storeAdmin) {
        if (storeDto == null) {
            return null;
        }

        Store store = new Store();
        // Don't set ID when creating new entities - let JPA generate it
        // Only set ID if it's an update operation and ID already exists
        if (storeDto.getId() != null) {
            store.setId(storeDto.getId());
        }

        store.setBrandName(storeDto.getBrandName());
        store.setDescription(storeDto.getDescription());
        store.setStoreAdmin(storeAdmin);
        store.setStoreType(storeDto.getStoreType());
        store.setContact(storeDto.getContact());

        // Don't set createdDate and updatedDate - let @PrePersist and @PreUpdate handle them
        // store.setCreatedDate(storeDto.getCreatedDate());
        // store.setUpdatedDate(storeDto.getUpdatedDate());

        store.setStatus(storeDto.getStatus());
        return store;
    }
}