package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.payload.dto.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(UUID id) throws Exception;

    // Added
    User save(User user);
    User findById(UUID id) throws UserException;

    List<User> getAllUsers(List<User> users);
    List<OrderDTO> getAllUsers();
}