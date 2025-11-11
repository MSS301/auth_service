package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.request.SelfClassRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.ClassResponse;
import com.auth_svc.auth.dto.response.PaginatedResponse;
import com.auth_svc.auth.service.ClassService;
import com.auth_svc.auth.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Class Management", description = "APIs for class management")
public class ClassController {
    ClassService classService;
    UserProfileService userProfileService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Create a new class", description = "Creates a new class")
    public ApiResponse<ClassResponse> createClass(@Valid @RequestBody ClassRequest request) {
        log.info("REST request to create class");
        return ApiResponse.<ClassResponse>builder()
                .result(classService.createClass(request))
                .build();
    }

    @PostMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(
            summary = "Create class for current teacher",
            description = "Teacher creates a class for themselves using their JWT token")
    public ApiResponse<ClassResponse> createSelfClass(@Valid @RequestBody SelfClassRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        log.info("REST request for teacher to create their own class, user ID: {}", currentUserId);
        return ApiResponse.<ClassResponse>builder()
                .result(classService.createSelfClass(request, currentUserId))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassResponse> getClassById(@PathVariable Integer id) {
        log.info("REST request to get class by id: {}", id);
        return ApiResponse.<ClassResponse>builder()
                .result(classService.getClassById(id))
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(summary = "Get my classes", description = "Teacher gets all their classes with pagination")
    public PaginatedResponse<ClassResponse> getMyClasses(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName();

        log.info("REST request for teacher to get their classes, user ID: {}", currentUserId);
        Page<ClassResponse> page = classService.getMyClasses(currentUserId, pageable);
        return PaginatedResponse.of(page);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all classes with pagination",
            description = "Returns paginated list of classes with optional filters")
    public PaginatedResponse<ClassResponse> getAllClasses(
            @RequestParam(required = false) Integer schoolId,
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) Integer grade,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("REST request to get all classes with pagination");

        Page<ClassResponse> page;

        if (name != null && !name.isEmpty()) {
            page = classService.searchClassesByName(name, pageable);
        } else if (schoolId != null && grade != null) {
            page = classService.getClassesBySchoolAndGrade(schoolId, grade, pageable);
        } else if (schoolId != null) {
            page = classService.getClassesBySchool(schoolId, pageable);
        } else if (teacherId != null) {
            page = classService.getClassesByTeacher(teacherId, pageable);
        } else if (grade != null) {
            page = classService.getClassesByGrade(grade, pageable);
        } else {
            page = classService.getAllClasses(pageable);
        }

        return PaginatedResponse.of(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ClassResponse> updateClass(@PathVariable Integer id, @Valid @RequestBody ClassRequest request) {
        log.info("REST request to update class id: {}", id);
        return ApiResponse.<ClassResponse>builder()
                .result(classService.updateClass(id, request))
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Search classes by school and name",
            description = "Students can search for classes in their school")
    public ApiResponse<List<ClassResponse>> searchClasses(@RequestParam Integer schoolId, @RequestParam String name) {
        log.info("REST request to search classes in school {} with name: {}", schoolId, name);
        return ApiResponse.<List<ClassResponse>>builder()
                .result(classService.searchClassesBySchoolAndName(schoolId, name))
                .build();
    }

    @GetMapping("/search/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Search classes in my school",
            description = "Students can search for classes in their own school by name")
    public ApiResponse<List<ClassResponse>> searchMySchoolClasses(@RequestParam String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        var userProfile = userProfileService.getUserProfileByAccountId(currentUserId);
        Integer schoolId = userProfile.getSchoolId();

        log.info("REST request to search classes in my school {} with name: {}", schoolId, name);
        return ApiResponse.<List<ClassResponse>>builder()
                .result(classService.searchClassesBySchoolAndName(schoolId, name))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "Delete a class", description = "Deletes a class by ID")
    public ApiResponse<Void> deleteClass(@PathVariable Integer id) {
        log.info("REST request to delete class id: {}", id);
        classService.deleteClass(id);
        return ApiResponse.<Void>builder().message("Class deleted successfully").build();
    }
}
