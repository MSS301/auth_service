package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.UserRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.UserResponse;
import com.auth_svc.auth.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>> listUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getById(@PathVariable("id") String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<UserResponse> create(@RequestBody @Valid UserRequest request) {
        var req = new com.auth_svc.auth.dto.request.UserCreationRequest();
        req.setUsername(request.getUsername());
        req.setEmail(request.getEmail());
        req.setPassword(request.getPassword());
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(req))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> update(
            @PathVariable("id") String id, @RequestBody com.auth_svc.auth.dto.request.UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ApiResponse<String> delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PostMapping("/{id}/promote/teacher")
    ApiResponse<UserResponse> promoteToTeacher(@PathVariable("id") String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.promoteToTeacher(id))
                .build();
    }
}
