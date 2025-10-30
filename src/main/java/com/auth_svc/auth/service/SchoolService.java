package com.auth_svc.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth_svc.auth.dto.request.SchoolRequest;
import com.auth_svc.auth.dto.response.SchoolResponse;

public interface SchoolService {

    SchoolResponse createSchool(SchoolRequest request);

    SchoolResponse getSchoolById(Integer id);

    //    List<SchoolResponse> getAllSchools();

    Page<SchoolResponse> getAllSchools(Pageable pageable);

    //    List<SchoolResponse> searchSchoolsByName(String name);

    Page<SchoolResponse> searchSchoolsByName(String name, Pageable pageable);

    SchoolResponse updateSchool(Integer id, SchoolRequest request);

    void deleteSchool(Integer id);
}
