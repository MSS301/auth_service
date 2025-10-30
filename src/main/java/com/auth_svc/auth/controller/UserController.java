package com.auth_svc.auth.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.UserRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.PaginatedResponse;
import com.auth_svc.auth.dto.response.UserResponse;
import com.auth_svc.auth.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all users with pagination",
            description = "Returns paginated list of users with optional search filters")
    PaginatedResponse<UserResponse> listUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<UserResponse> page;

        if (email != null && !email.isEmpty()) {
            page = userService.searchUsersByEmail(email, pageable);
        } else if (username != null && !username.isEmpty()) {
            page = userService.searchUsersByUsername(username, pageable);
        } else {
            page = userService.getUsers(pageable);
        }

        return PaginatedResponse.of(page);
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
