package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.ClassResponse;
import com.auth_svc.auth.service.ClassService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClassController {
    ClassService classService;

    @PostMapping
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
    public ApiResponse<List<ClassResponse>> getAllClasses(
            @RequestParam(required = false) Integer schoolId,
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) Integer grade) {
        log.info("REST request to get all classes");

        if (schoolId != null && grade != null) {
            return ApiResponse.<List<ClassResponse>>builder()
                    .result(classService.getClassesBySchoolAndGrade(schoolId, grade))
                    .build();
        } else if (schoolId != null) {
            return ApiResponse.<List<ClassResponse>>builder()
                    .result(classService.getClassesBySchool(schoolId))
                    .build();
        } else if (teacherId != null) {
            return ApiResponse.<List<ClassResponse>>builder()
                    .result(classService.getClassesByTeacher(teacherId))
                    .build();
        } else if (grade != null) {
            return ApiResponse.<List<ClassResponse>>builder()
                    .result(classService.getClassesByGrade(grade))
                    .build();
        }

        return ApiResponse.<List<ClassResponse>>builder()
                .result(classService.getAllClasses())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ClassResponse> updateClass(@PathVariable Integer id, @Valid @RequestBody ClassRequest request) {
        log.info("REST request to update class id: {}", id);
        return ApiResponse.<ClassResponse>builder()
                .result(classService.updateClass(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClass(@PathVariable Integer id) {
        log.info("REST request to delete class id: {}", id);
        classService.deleteClass(id);
        return ApiResponse.<Void>builder().message("Class deleted successfully").build();
    }
}
