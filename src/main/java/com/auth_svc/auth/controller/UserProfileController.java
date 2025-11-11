package com.auth_svc.auth.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.auth_svc.auth.dto.request.SelfUserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileUpdateRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.PaginatedResponse;
import com.auth_svc.auth.dto.response.UserProfileResponse;
import com.auth_svc.auth.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create user profile for current user",
            description =
                    "Allows a user to create their own profile using their JWT token and optionally upload teacher proof")
    public ApiResponse<UserProfileResponse> createSelfUserProfile(
            @Valid @ModelAttribute SelfUserProfileRequest request,
            @RequestPart(name = "teacherProof", required = false) MultipartFile teacherProof,
            @RequestPart(name = "avatar", required = false) MultipartFile avatar)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // This gets the "sub" claim from JWT

        // Extract role from token authorities (remove "ROLE_" prefix)
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // Remove "ROLE_" prefix
                .findFirst()
                .orElse(null);

        log.info("REST request for user to create their own profile, user ID: {}, role: {}", currentUserId, role);

        String teacherProofPath = null;
        if (teacherProof != null && !teacherProof.isEmpty()) {
            teacherProofPath = saveTeacherProof(teacherProof);
        }

        String avatarPath = null;
        if (avatar != null && !avatar.isEmpty()) {
            avatarPath = saveTeacherProof(avatar); // reuse same storage method
        }

        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createSelfUserProfile(
                        request, currentUserId, role, teacherProofPath, avatarPath))
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
    @Operation(
            summary = "Get all user profiles",
            description = "Retrieve paginated list of user profiles with optional filtering by school and/or role")
    public PaginatedResponse<UserProfileResponse> getAllUserProfiles(
            @Parameter(description = "Filter by school ID") @RequestParam(required = false) Integer schoolId,
            @Parameter(description = "Filter by role") @RequestParam(required = false) String role,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20, sort = "id")
                    Pageable pageable) {
        log.info("REST request to get all user profiles with pagination");

        Page<UserProfileResponse> userProfiles;
        if (schoolId != null && role != null) {
            userProfiles = userProfileService.getUserProfilesBySchoolAndRole(schoolId, role, pageable);
        } else if (schoolId != null) {
            userProfiles = userProfileService.getUserProfilesBySchool(schoolId, pageable);
        } else if (role != null) {
            userProfiles = userProfileService.getUserProfilesByRole(role, pageable);
        } else {
            userProfiles = userProfileService.getAllUserProfiles(pageable);
        }

        return PaginatedResponse.of(userProfiles);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Integer id,
            @Valid @ModelAttribute UserProfileUpdateRequest request,
            @RequestPart(name = "teacherProof", required = false) MultipartFile teacherProof)
            throws IOException {
        log.info("REST request to update user profile id: {}", id);

        String savedPath = null;
        if (teacherProof != null && !teacherProof.isEmpty()) {
            savedPath = saveTeacherProof(teacherProof);
        }

        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateUserProfile(id, request, savedPath))
                .build();
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update own profile", description = "User updates their own profile using their JWT token")
    public ApiResponse<UserProfileResponse> updateSelfUserProfile(
            @Valid @ModelAttribute UserProfileUpdateRequest request,
            @RequestPart(name = "teacherProof", required = false) MultipartFile teacherProof)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        log.info("REST request for user to update their own profile, user ID: {}", currentUserId);

        String savedPath = null;
        if (teacherProof != null && !teacherProof.isEmpty()) {
            savedPath = saveTeacherProof(teacherProof);
        }

        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.updateSelfUserProfile(request, currentUserId, savedPath))
                .build();
    }

    private String saveTeacherProof(MultipartFile file) throws IOException {
        // Simple local storage. Production should use S3 or other storage and sanitize filenames.
        String uploadDir = System.getProperty("user.dir") + "/uploads/teacher_proofs";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = Instant.now().toEpochMilli() + "_" + file.getOriginalFilename();
        Path target = uploadPath.resolve(filename).normalize();
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
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
