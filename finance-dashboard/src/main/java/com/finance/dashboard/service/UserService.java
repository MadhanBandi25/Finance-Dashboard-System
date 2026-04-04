package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.CreateUserRequest;
import com.finance.dashboard.dto.request.UpdateUserRequest;
import com.finance.dashboard.dto.request.UserStatusRequest;
import com.finance.dashboard.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse createUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);
    UserResponse updateUserStatus(Long id, UserStatusRequest request);

    UserResponse getUserByEmail(String email);

    void deleteUser(Long id);
}
