package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.UserProfileRequest;
import com.auth_svc.auth.dto.request.UserProfileUpdateRequest;
import com.auth_svc.auth.dto.response.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserProfileRequest request);

    UserProfileResponse getUserProfileById(Integer id);

    UserProfileResponse getUserProfileByAccountId(String accountId);

    List<UserProfileResponse> getAllUserProfiles();

    List<UserProfileResponse> getUserProfilesBySchool(Integer schoolId);

    List<UserProfileResponse> getUserProfilesByRole(String role);

    List<UserProfileResponse> getUserProfilesBySchoolAndRole(Integer schoolId, String role);

    UserProfileResponse updateUserProfile(Integer id, UserProfileUpdateRequest request);

    void deleteUserProfile(Integer id);
}
