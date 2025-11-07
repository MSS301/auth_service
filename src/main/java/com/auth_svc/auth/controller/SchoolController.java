package com.auth_svc.auth.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.SchoolRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.PaginatedResponse;
import com.auth_svc.auth.dto.response.SchoolResponse;
import com.auth_svc.auth.service.SchoolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "School Management", description = "APIs for school management")
public class SchoolController {
    SchoolService schoolService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new school", description = "Creates a new school")
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
    @Operation(
            summary = "Get all schools with pagination",
            description = "Returns paginated list of schools with optional name search")
    public PaginatedResponse<SchoolResponse> getAllSchools(
            @RequestParam(required = false) String name, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        log.info("REST request to get all schools with pagination");

        Page<SchoolResponse> page;

        if (name != null && !name.isEmpty()) {
            page = schoolService.searchSchoolsByName(name, pageable);
        } else {
            page = schoolService.getAllSchools(pageable);
        }

        return PaginatedResponse.of(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SchoolResponse> updateSchool(
            @PathVariable Integer id, @Valid @RequestBody SchoolRequest request) {
        log.info("REST request to update school id: {}", id);
        return ApiResponse.<SchoolResponse>builder()
                .result(schoolService.updateSchool(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a school", description = "Deletes a school by ID")
    public ApiResponse<Void> deleteSchool(@PathVariable Integer id) {
        log.info("REST request to delete school id: {}", id);
        schoolService.deleteSchool(id);
        return ApiResponse.<Void>builder()
                .message("School deleted successfully")
                .build();
    }
}
