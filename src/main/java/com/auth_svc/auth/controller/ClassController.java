package com.auth_svc.auth.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.ClassResponse;
import com.auth_svc.auth.dto.response.PaginatedResponse;
import com.auth_svc.auth.service.ClassService;

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new class", description = "Creates a new class")
    public ApiResponse<ClassResponse> createClass(@Valid @RequestBody ClassRequest request) {
        log.info("REST request to create class");
        return ApiResponse.<ClassResponse>builder()
                .result(classService.createClass(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ClassResponse> getClassById(@PathVariable Integer id) {
        log.info("REST request to get class by id: {}", id);
        return ApiResponse.<ClassResponse>builder()
                .result(classService.getClassById(id))
                .build();
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a class", description = "Deletes a class by ID")
    public ApiResponse<Void> deleteClass(@PathVariable Integer id) {
        log.info("REST request to delete class id: {}", id);
        classService.deleteClass(id);
        return ApiResponse.<Void>builder().message("Class deleted successfully").build();
    }
}
