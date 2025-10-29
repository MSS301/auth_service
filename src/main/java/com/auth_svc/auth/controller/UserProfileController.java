package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.UserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileUpdateRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.UserProfileResponse;
import com.auth_svc.auth.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user-profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping
    public ApiResponse<UserProfileResponse> createUserProfile(@Valid @RequestBody UserProfileRequest request) {
        log.info("REST request to create user profile");
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createUserProfile(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // This gets the "sub" claim from JWT

        log.info("REST request to get current user profile for user ID: {}", currentUserId);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getUserProfileByAccountId(currentUserId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserProfileResponse> getUserProfileById(@PathVariable Integer id) {
        log.info("REST request to get user profile by id: {}", id);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getUserProfileById(id))
                .build();
    }

    @GetMapping("/account/{accountId}")
    public ApiResponse<UserProfileResponse> getUserProfileByAccountId(@PathVariable String accountId) {
        log.info("REST request to get user profile by account ID: {}", accountId);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.getUserProfileByAccountId(accountId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserProfileResponse>> getAllUserProfiles(
            @RequestParam(required = false) Integer schoolId, @RequestParam(required = false) String role) {
        log.info("REST request to get all user profiles");

        if (schoolId != null && role != null) {
            return ApiResponse.<List<UserProfileResponse>>builder()
                    .result(userProfileService.getUserProfilesBySchoolAndRole(schoolId, role))
                    .build();
        } else if (schoolId != null) {
            return ApiResponse.<List<UserProfileResponse>>builder()
                    .result(userProfileService.getUserProfilesBySchool(schoolId))
                    .build();
        } else if (role != null) {
            return ApiResponse.<List<UserProfileResponse>>builder()
                    .result(userProfileService.getUserProfilesByRole(role))
                    .build();
        }

        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllUserProfiles())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Integer id, @Valid @RequestBody UserProfileUpdateRequest request) {
        log.info("REST request to update user profile id: {}", id);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateUserProfile(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUserProfile(@PathVariable Integer id) {
        log.info("REST request to delete user profile id: {}", id);
        userProfileService.deleteUserProfile(id);
        return ApiResponse.<Void>builder()
                .message("User profile deleted successfully")
                .build();
    }
}
