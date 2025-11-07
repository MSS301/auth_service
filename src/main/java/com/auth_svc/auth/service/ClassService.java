package com.auth_svc.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.request.SelfClassRequest;
import com.auth_svc.auth.dto.response.ClassResponse;

public interface ClassService {

    ClassResponse createClass(ClassRequest request);

    ClassResponse createSelfClass(SelfClassRequest request, String accountId);

    java.util.List<ClassResponse> getMyClasses(String accountId);

    ClassResponse getClassById(Integer id);

    //    List<ClassResponse> getAllClasses();

    Page<ClassResponse> getAllClasses(Pageable pageable);

    //    List<ClassResponse> getClassesBySchool(Integer schoolId);

    Page<ClassResponse> getClassesBySchool(Integer schoolId, Pageable pageable);

    //    List<ClassResponse> getClassesByTeacher(Integer teacherId);

    Page<ClassResponse> getClassesByTeacher(Integer teacherId, Pageable pageable);

    //    List<ClassResponse> getClassesByGrade(Integer grade);

    Page<ClassResponse> getClassesByGrade(Integer grade, Pageable pageable);

    //    List<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade);

    Page<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade, Pageable pageable);

    Page<ClassResponse> searchClassesByName(String name, Pageable pageable);

    java.util.List<ClassResponse> searchClassesBySchoolAndName(Integer schoolId, String name);

    ClassResponse updateClass(Integer id, ClassRequest request);

    void deleteClass(Integer id);
}
