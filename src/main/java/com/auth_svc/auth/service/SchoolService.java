package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.SchoolRequest;
import com.auth_svc.auth.dto.response.SchoolResponse;

public interface SchoolService {

    SchoolResponse createSchool(SchoolRequest request);

    SchoolResponse getSchoolById(Integer id);

    List<SchoolResponse> getAllSchools();

    List<SchoolResponse> searchSchoolsByName(String name);

    SchoolResponse updateSchool(Integer id, SchoolRequest request);

    void deleteSchool(Integer id);
}
