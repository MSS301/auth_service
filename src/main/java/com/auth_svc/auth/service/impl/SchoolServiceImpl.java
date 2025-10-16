package com.auth_svc.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.SchoolRequest;
import com.auth_svc.auth.dto.response.SchoolResponse;
import com.auth_svc.auth.entity.School;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.SchoolRepository;
import com.auth_svc.auth.service.SchoolService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SchoolServiceImpl implements SchoolService {
    SchoolRepository schoolRepository;

    @Transactional
    @Override
    public SchoolResponse createSchool(SchoolRequest request) {
        log.info("Creating new school: {}", request.getName());

        if (schoolRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SCHOOL_ALREADY_EXISTS);
        }

        School school = School.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        school = schoolRepository.save(school);
        return mapToResponse(school);
    }

    @Override
    public SchoolResponse getSchoolById(Integer id) {
        log.info("Getting school by id: {}", id);
        School school = schoolRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
        return mapToResponse(school);
    }

    @Override
    public List<SchoolResponse> getAllSchools() {
        log.info("Getting all schools");
        return schoolRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<SchoolResponse> searchSchoolsByName(String name) {
        log.info("Searching schools by name: {}", name);
        return schoolRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SchoolResponse updateSchool(Integer id, SchoolRequest request) {
        log.info("Updating school id: {}", id);
        School school = schoolRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));

        if (!school.getName().equals(request.getName()) && schoolRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SCHOOL_ALREADY_EXISTS);
        }

        school.setName(request.getName());
        school.setAddress(request.getAddress());

        school = schoolRepository.save(school);
        return mapToResponse(school);
    }

    @Transactional
    @Override
    public void deleteSchool(Integer id) {
        log.info("Deleting school id: {}", id);
        if (!schoolRepository.existsById(id)) {
            throw new AppException(ErrorCode.SCHOOL_NOT_FOUND);
        }
        schoolRepository.deleteById(id);
    }

    private SchoolResponse mapToResponse(School school) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .build();
    }
}
