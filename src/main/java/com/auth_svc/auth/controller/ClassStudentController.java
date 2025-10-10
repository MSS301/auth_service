package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.ClassStudentRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.ClassStudentResponse;
import com.auth_svc.auth.service.ClassStudentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/class-students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClassStudentController {
    ClassStudentService classStudentService;

    @PostMapping
    public ApiResponse<ClassStudentResponse> enrollStudent(@Valid @RequestBody ClassStudentRequest request) {
        log.info("REST request to enroll student");
        return ApiResponse.<ClassStudentResponse>builder()
                .result(classStudentService.enrollStudent(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassStudentResponse> getEnrollmentById(@PathVariable Integer id) {
        log.info("REST request to get enrollment by id: {}", id);
        return ApiResponse.<ClassStudentResponse>builder()
                .result(classStudentService.getEnrollmentById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ClassStudentResponse>> getAllEnrollments(
            @RequestParam(required = false) Integer classId, @RequestParam(required = false) Integer studentId) {
        log.info("REST request to get all enrollments");

        if (classId != null) {
            return ApiResponse.<List<ClassStudentResponse>>builder()
                    .result(classStudentService.getEnrollmentsByClass(classId))
                    .build();
        } else if (studentId != null) {
            return ApiResponse.<List<ClassStudentResponse>>builder()
                    .result(classStudentService.getEnrollmentsByStudent(studentId))
                    .build();
        }

        return ApiResponse.<List<ClassStudentResponse>>builder()
                .result(classStudentService.getAllEnrollments())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEnrollment(@PathVariable Integer id) {
        log.info("REST request to delete enrollment id: {}", id);
        classStudentService.deleteEnrollment(id);
        return ApiResponse.<Void>builder()
                .message("Enrollment deleted successfully")
                .build();
    }

    @DeleteMapping("/unenroll")
    public ApiResponse<Void> unenrollStudent(@RequestParam Integer classId, @RequestParam Integer studentId) {
        log.info("REST request to unenroll student {} from class {}", studentId, classId);
        classStudentService.unenrollStudent(classId, studentId);
        return ApiResponse.<Void>builder()
                .message("Student unenrolled successfully")
                .build();
    }
}
