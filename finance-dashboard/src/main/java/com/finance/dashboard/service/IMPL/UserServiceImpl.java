package com.finance.dashboard.service.IMPL;

import com.finance.dashboard.dto.request.CreateUserRequest;
import com.finance.dashboard.dto.request.UpdateUserRequest;
import com.finance.dashboard.dto.request.UserStatusRequest;
import com.finance.dashboard.dto.response.UserResponse;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Role;
import com.finance.dashboard.enums.UserStatus;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.mapper.UserMapper;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserResponse createUser(CreateUserRequest request) {


        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        User user = UserMapper.toEntity(request);
        if (user.getRole() == null){
            user.setRole(Role.VIEWER);
        }

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }


    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ResourceNotFoundException("User is deleted");
        }

        return UserMapper.toResponse(user);
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Cannot update deleted user");
        }

        if (request.getEmail() != null &&
                !request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())){

            throw new IllegalArgumentException("Email already in use: "+ request.getEmail());
        }

        UserMapper.updateEntity(user,request);
        User updateUser = userRepository.save(user);
        return UserMapper.toResponse(updateUser);
    }


    @Override
    public UserResponse updateUserStatus(Long id, UserStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getStatus() == request.getStatus()) {
            throw new IllegalArgumentException("User already in this status");
        }

        user.setStatus(request.getStatus());
        User updateUser = userRepository.save(user);
        return UserMapper.toResponse(updateUser);
    }


    @Override
    public  UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow( ()-> new ResourceNotFoundException("User not found with email: "+ email));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new ResourceNotFoundException("User is deleted");
        }
        return UserMapper.toResponse(user);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow( ()-> new ResourceNotFoundException("User not found with id: "+ id));

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("User already deleted");
        }
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }
}
