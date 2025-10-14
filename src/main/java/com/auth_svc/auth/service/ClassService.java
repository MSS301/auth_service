package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.response.ClassResponse;

public interface ClassService {

    ClassResponse createClass(ClassRequest request);

    ClassResponse getClassById(Integer id);

    List<ClassResponse> getAllClasses();

    List<ClassResponse> getClassesBySchool(Integer schoolId);

    List<ClassResponse> getClassesByTeacher(Integer teacherId);

    List<ClassResponse> getClassesByGrade(Integer grade);

    List<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade);

    ClassResponse updateClass(Integer id, ClassRequest request);

    void deleteClass(Integer id);
}
