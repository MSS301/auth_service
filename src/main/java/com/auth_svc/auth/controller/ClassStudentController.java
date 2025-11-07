package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.ClassStudentRequest;
import com.auth_svc.auth.dto.request.EnrollmentRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.ClassStudentResponse;
import com.auth_svc.auth.service.ClassStudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/class-students")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Class Student Management", description = "APIs for managing class enrollments")
public class ClassStudentController {
    ClassStudentService classStudentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Enroll student (Admin/Teacher)", description = "Admin or teacher enrolls a student to class")
    public ApiResponse<ClassStudentResponse> enrollStudent(@Valid @RequestBody ClassStudentRequest request) {
        log.info("REST request to enroll student");
        return ApiResponse.<ClassStudentResponse>builder()
                .result(classStudentService.enrollStudent(request))
                .build();
    }

    @PostMapping("/enroll")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Student self-enrollment", description = "Student enrolls in class with password")
    public ApiResponse<ClassStudentResponse> enrollWithPassword(
            @Valid @RequestBody EnrollmentRequest request, Authentication authentication) {
        log.info("REST request for student self-enrollment");
        // Extract student ID from JWT token (assuming it's in the principal)
        String studentId = authentication.getName();
        return ApiResponse.<ClassStudentResponse>builder()
                .result(classStudentService.enrollWithPassword(request, studentId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassStudentResponse> getEnrollmentById(@PathVariable Integer id) {
        log.info("REST request to get enrollment by id: {}", id);
        return ApiResponse.<ClassStudentResponse>builder()
                .result(classStudentService.getEnrollmentById(id))
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my enrollments", description = "Student gets all their enrolled classes")
    public ApiResponse<List<ClassStudentResponse>> getMyEnrollments(Authentication authentication) {
        String userId = authentication.getName();

        log.info("REST request for student to get their enrollments, user ID: {}", userId);
        return ApiResponse.<List<ClassStudentResponse>>builder()
                .result(classStudentService.getMyEnrollments(userId))
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

    @DeleteMapping("/me/unenroll")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Unenroll self from class", description = "Student unenrolls themselves from a class")
    public ApiResponse<Void> unenrollSelf(@RequestParam Integer classId, Authentication authentication) {
        String userId = authentication.getName();

        log.info("REST request for student to unenroll themselves from class {}, user ID: {}", classId, userId);
        classStudentService.unenrollSelf(classId, userId);
        return ApiResponse.<Void>builder()
                .message("Successfully unenrolled from class")
                .build();
    }
}
