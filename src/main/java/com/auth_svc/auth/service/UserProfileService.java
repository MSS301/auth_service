package com.auth_svc.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.UserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileUpdateRequest;
import com.auth_svc.auth.dto.response.UserProfileResponse;
import com.auth_svc.auth.entity.School;
import com.auth_svc.auth.entity.UserProfile;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.SchoolRepository;
import com.auth_svc.auth.repository.UserProfileRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    SchoolRepository schoolRepository;

    @Transactional
    public UserProfileResponse createUserProfile(UserProfileRequest request) {
        log.info("Creating new user profile for account ID: {}", request.getAccountId());

        if (userProfileRepository.existsByAccountId(request.getAccountId())) {
            throw new AppException(ErrorCode.USER_PROFILE_ALREADY_EXISTS);
        }

        UserProfile userProfile = UserProfile.builder()
                .accountId(request.getAccountId())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .avatarUrl(request.getAvatarUrl())
                .role(request.getRole())
                .build();

        if (request.getSchoolId() != null) {
            School school = schoolRepository
                    .findById(request.getSchoolId())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
            userProfile.setSchool(school);
        }

        userProfile = userProfileRepository.save(userProfile);
        return mapToResponse(userProfile);
    }

    public UserProfileResponse getUserProfileById(Integer id) {
        log.info("Getting user profile by id: {}", id);
        UserProfile userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
        return mapToResponse(userProfile);
    }

    public UserProfileResponse getUserProfileByAccountId(Integer accountId) {
        log.info("Getting user profile by account ID: {}", accountId);
        UserProfile userProfile = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
        return mapToResponse(userProfile);
    }

    public List<UserProfileResponse> getAllUserProfiles() {
        log.info("Getting all user profiles");
        return userProfileRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<UserProfileResponse> getUserProfilesBySchool(Integer schoolId) {
        log.info("Getting user profiles by school ID: {}", schoolId);
        return userProfileRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UserProfileResponse> getUserProfilesByRole(String role) {
        log.info("Getting user profiles by role: {}", role);
        return userProfileRepository.findByRole(role).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<UserProfileResponse> getUserProfilesBySchoolAndRole(Integer schoolId, String role) {
        log.info("Getting user profiles by school ID: {} and role: {}", schoolId, role);
        return userProfileRepository.findBySchoolIdAndRole(schoolId, role).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Integer id, UserProfileUpdateRequest request) {
        log.info("Updating user profile id: {}", id);
        UserProfile userProfile = userProfileRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        if (request.getFullName() != null) {
            userProfile.setFullName(request.getFullName());
        }
        if (request.getDateOfBirth() != null) {
            userProfile.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getAvatarUrl() != null) {
            userProfile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getRole() != null) {
            userProfile.setRole(request.getRole());
        }
        if (request.getSchoolId() != null) {
            School school = schoolRepository
                    .findById(request.getSchoolId())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
            userProfile.setSchool(school);
        }

        userProfile = userProfileRepository.save(userProfile);
        return mapToResponse(userProfile);
    }

    @Transactional
    public void deleteUserProfile(Integer id) {
        log.info("Deleting user profile id: {}", id);
        if (!userProfileRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_PROFILE_NOT_FOUND);
        }
        userProfileRepository.deleteById(id);
    }

    private UserProfileResponse mapToResponse(UserProfile userProfile) {
        return UserProfileResponse.builder()
                .id(userProfile.getId())
                .accountId(userProfile.getAccountId())
                .schoolId(
                        userProfile.getSchool() != null
                                ? userProfile.getSchool().getId()
                                : null)
                .schoolName(
                        userProfile.getSchool() != null
                                ? userProfile.getSchool().getName()
                                : null)
                .fullName(userProfile.getFullName())
                .dateOfBirth(userProfile.getDateOfBirth())
                .avatarUrl(userProfile.getAvatarUrl())
                .role(userProfile.getRole())
                .createdAt(userProfile.getCreatedAt())
                .updatedAt(userProfile.getUpdatedAt())
                .build();
    }
}
