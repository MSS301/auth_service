package com.auth_svc.auth.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.SchoolRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.SchoolResponse;
import com.auth_svc.auth.service.SchoolService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SchoolController {
    SchoolService schoolService;

    @PostMapping
    public ApiResponse<SchoolResponse> createSchool(@Valid @RequestBody SchoolRequest request) {
        log.info("REST request to create school");
        return ApiResponse.<SchoolResponse>builder()
                .result(schoolService.createSchool(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SchoolResponse> getSchoolById(@PathVariable Integer id) {
        log.info("REST request to get school by id: {}", id);
        return ApiResponse.<SchoolResponse>builder()
                .result(schoolService.getSchoolById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SchoolResponse>> getAllSchools(@RequestParam(required = false) String name) {
        log.info("REST request to get all schools");

        if (name != null && !name.isEmpty()) {
            return ApiResponse.<List<SchoolResponse>>builder()
                    .result(schoolService.searchSchoolsByName(name))
                    .build();
        }

        return ApiResponse.<List<SchoolResponse>>builder()
                .result(schoolService.getAllSchools())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SchoolResponse> updateSchool(
            @PathVariable Integer id, @Valid @RequestBody SchoolRequest request) {
        log.info("REST request to update school id: {}", id);
        return ApiResponse.<SchoolResponse>builder()
                .result(schoolService.updateSchool(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSchool(@PathVariable Integer id) {
        log.info("REST request to delete school id: {}", id);
        schoolService.deleteSchool(id);
        return ApiResponse.<Void>builder()
                .message("School deleted successfully")
                .build();
    }
}
