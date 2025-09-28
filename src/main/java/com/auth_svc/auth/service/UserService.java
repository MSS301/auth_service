package com.auth_svc.auth.service;

import com.auth_svc.auth.dto.request.UserCreationRequest;
import com.auth_svc.auth.dto.request.UserUpdateRequest;
import com.auth_svc.auth.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
    List<UserResponse> getUsers();
    UserResponse getUser(String id);
    UserResponse getMyInfo();
}