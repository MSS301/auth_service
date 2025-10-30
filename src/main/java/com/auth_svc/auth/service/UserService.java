package com.auth_svc.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth_svc.auth.dto.request.UserCreationRequest;
import com.auth_svc.auth.dto.request.UserUpdateRequest;
import com.auth_svc.auth.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    //    List<UserResponse> getUsers();

    Page<UserResponse> getUsers(Pageable pageable);

    Page<UserResponse> searchUsersByEmail(String email, Pageable pageable);

    Page<UserResponse> searchUsersByUsername(String username, Pageable pageable);

    UserResponse getUser(String id);

    //    UserResponse getMyInfo();

    UserResponse promoteToTeacher(String userId);
}
