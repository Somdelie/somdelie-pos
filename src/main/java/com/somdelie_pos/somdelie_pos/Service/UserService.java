package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.exceptions.UserException;
import com.somdelie_pos.somdelie_pos.modal.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(UUID id) throws UserException, Exception;
    List<User> getAllUsers();
}