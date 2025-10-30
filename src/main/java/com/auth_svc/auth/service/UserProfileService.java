package com.auth_svc.auth.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth_svc.auth.dto.request.UserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileUpdateRequest;
import com.auth_svc.auth.dto.response.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileRequest request);

    UserProfileResponse getUserProfileById(Integer id);

    UserProfileResponse getUserProfileByAccountId(String accountId);

    List<UserProfileResponse> getAllUserProfiles();

    Page<UserProfileResponse> getAllUserProfiles(Pageable pageable);

    //    List<UserProfileResponse> getUserProfilesBySchool(Integer schoolId);

    Page<UserProfileResponse> getUserProfilesBySchool(Integer schoolId, Pageable pageable);

    //    List<UserProfileResponse> getUserProfilesByRole(String role);

    Page<UserProfileResponse> getUserProfilesByRole(String role, Pageable pageable);

    //    List<UserProfileResponse> getUserProfilesBySchoolAndRole(Integer schoolId, String role);

    Page<UserProfileResponse> getUserProfilesBySchoolAndRole(Integer schoolId, String role, Pageable pageable);

    UserProfileResponse updateUserProfile(Integer id, UserProfileUpdateRequest request);

    void deleteUserProfile(Integer id);
}
