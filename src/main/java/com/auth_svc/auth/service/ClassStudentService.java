package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.ClassStudentRequest;
import com.auth_svc.auth.dto.response.ClassStudentResponse;

public interface ClassStudentService {

    ClassStudentResponse enrollStudent(ClassStudentRequest request);

    ClassStudentResponse getEnrollmentById(Integer id);

    List<ClassStudentResponse> getAllEnrollments();

    List<ClassStudentResponse> getEnrollmentsByClass(Integer classId);

    List<ClassStudentResponse> getEnrollmentsByStudent(Integer studentId);

    void unenrollStudent(Integer classId, Integer studentId);

    void deleteEnrollment(Integer id);
}
