package com.auth_svc.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.ClassRequest;
import com.auth_svc.auth.dto.request.SelfClassRequest;
import com.auth_svc.auth.dto.response.ClassResponse;
import com.auth_svc.auth.entity.Class;
import com.auth_svc.auth.entity.School;
import com.auth_svc.auth.entity.UserProfile;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.ClassRepository;
import com.auth_svc.auth.repository.SchoolRepository;
import com.auth_svc.auth.repository.UserProfileRepository;
import com.auth_svc.auth.service.ClassService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClassServiceImpl implements ClassService {
    ClassRepository classRepository;
    SchoolRepository schoolRepository;
    UserProfileRepository userProfileRepository;

    @Transactional
    @Override
    public ClassResponse createClass(ClassRequest request) {
        log.info("Creating new class: {}", request.getName());

        Class classEntity = Class.builder()
                .name(request.getName())
                .grade(request.getGrade())
                .password(request.getPassword())
                .build();

        if (request.getSchoolId() != null) {
            School school = schoolRepository
                    .findById(request.getSchoolId())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
            classEntity.setSchool(school);
        }

        if (request.getTeacherId() != null) {
            UserProfile teacher = userProfileRepository
                    .findById(request.getTeacherId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
            classEntity.setTeacher(teacher);
        }

        classEntity = classRepository.save(classEntity);
        return mapToResponse(classEntity);
    }

    @Transactional
    @Override
    public ClassResponse createSelfClass(SelfClassRequest request, String accountId) {
        log.info("Teacher creating their own class: {}, accountId: {}", request.getName(), accountId);

        // Find teacher's UserProfile by accountId
        UserProfile teacher = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        Class classEntity = Class.builder()
                .name(request.getName())
                .grade(request.getGrade())
                .password(request.getPassword())
                .teacher(teacher)
                .build();

        if (request.getSchoolId() != null) {
            School school = schoolRepository
                    .findById(request.getSchoolId())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
            classEntity.setSchool(school);
        }

        classEntity = classRepository.save(classEntity);
        log.info("Teacher {} successfully created class {}", accountId, classEntity.getId());
        return mapToResponse(classEntity);
    }

    @Override
    public ClassResponse getClassById(Integer id) {
        log.info("Getting class by id: {}", id);
        Class classEntity = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        return mapToResponse(classEntity);
    }

    @Override
    public List<ClassResponse> getMyClasses(String accountId) {
        log.info("Getting classes for teacher with accountId: {}", accountId);

        UserProfile teacher = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        return classRepository.findByTeacherId(teacher.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ClassResponse> getMyClasses(String accountId, Pageable pageable) {
        log.info("Getting classes for teacher with accountId: {} with pagination", accountId);

        UserProfile teacher = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        return classRepository.findByTeacherId(teacher.getId(), pageable).map(this::mapToResponse);
    }

    //    @Override
    //    public List<ClassResponse> getAllClasses() {
    //        log.info("Getting all classes");
    //        return classRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    //    }
    //
    //    @Override
    //    public List<ClassResponse> getClassesBySchool(Integer schoolId) {
    //        log.info("Getting classes by school ID: {}", schoolId);
    //        return classRepository.findBySchoolId(schoolId).stream()
    //                .map(this::mapToResponse)
    //                .collect(Collectors.toList());
    //    }
    //
    //    @Override
    //    public List<ClassResponse> getClassesByTeacher(Integer teacherId) {
    //        log.info("Getting classes by teacher ID: {}", teacherId);
    //        return classRepository.findByTeacherId(teacherId).stream()
    //                .map(this::mapToResponse)
    //                .collect(Collectors.toList());
    //    }
    //
    //    @Override
    //    public List<ClassResponse> getClassesByGrade(Integer grade) {
    //        log.info("Getting classes by grade: {}", grade);
    //        return classRepository.findByGrade(grade).stream()
    //                .map(this::mapToResponse)
    //                .collect(Collectors.toList());
    //    }
    //
    //    @Override
    //    public List<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade) {
    //        log.info("Getting classes by school ID: {} and grade: {}", schoolId, grade);
    //        return classRepository.findBySchoolIdAndGrade(schoolId, grade).stream()
    //                .map(this::mapToResponse)
    //                .collect(Collectors.toList());
    //    }

    @Transactional
    @Override
    public ClassResponse updateClass(Integer id, ClassRequest request) {
        log.info("Updating class id: {}", id);
        Class classEntity = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        if (request.getName() != null) {
            classEntity.setName(request.getName());
        }
        if (request.getGrade() != null) {
            classEntity.setGrade(request.getGrade());
        }
        if (request.getPassword() != null) {
            classEntity.setPassword(request.getPassword());
        }
        if (request.getSchoolId() != null) {
            School school = schoolRepository
                    .findById(request.getSchoolId())
                    .orElseThrow(() -> new AppException(ErrorCode.SCHOOL_NOT_FOUND));
            classEntity.setSchool(school);
        }
        if (request.getTeacherId() != null) {
            UserProfile teacher = userProfileRepository
                    .findById(request.getTeacherId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));
            classEntity.setTeacher(teacher);
        }

        classEntity = classRepository.save(classEntity);
        return mapToResponse(classEntity);
    }

    @Transactional
    @Override
    public void deleteClass(Integer id) {
        log.info("Deleting class id: {}", id);
        Class classEntity = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        classEntity.softDelete();
        classRepository.save(classEntity);
    }

    @Override
    public Page<ClassResponse> getAllClasses(Pageable pageable) {
        log.info(
                "Getting all classes with pagination - page: {}, size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());
        return classRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> getClassesBySchool(Integer schoolId, Pageable pageable) {
        log.info("Getting classes by school ID: {} with pagination", schoolId);
        return classRepository.findBySchoolId(schoolId, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> getClassesByTeacher(Integer teacherId, Pageable pageable) {
        log.info("Getting classes by teacher ID: {} with pagination", teacherId);
        return classRepository.findByTeacherId(teacherId, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> getClassesByGrade(Integer grade, Pageable pageable) {
        log.info("Getting classes by grade: {} with pagination", grade);
        return classRepository.findByGrade(grade, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade, Pageable pageable) {
        log.info("Getting classes by school ID: {} and grade: {} with pagination", schoolId, grade);
        return classRepository.findBySchoolIdAndGrade(schoolId, grade, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> searchClassesByName(String name, Pageable pageable) {
        log.info("Searching classes by name: {} with pagination", name);
        return classRepository.findByNameContainingIgnoreCase(name, pageable).map(this::mapToResponse);
    }

    @Override
    public List<ClassResponse> searchClassesBySchoolAndName(Integer schoolId, String name) {
        log.info("Searching classes by school ID: {} and name: {}", schoolId, name);
        return classRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ClassResponse mapToResponse(Class classEntity) {
        return ClassResponse.builder()
                .id(classEntity.getId())
                .schoolId(
                        classEntity.getSchool() != null
                                ? classEntity.getSchool().getId()
                                : null)
                .schoolName(
                        classEntity.getSchool() != null
                                ? classEntity.getSchool().getName()
                                : null)
                .name(classEntity.getName())
                .grade(classEntity.getGrade())
                .teacherId(
                        classEntity.getTeacher() != null
                                ? classEntity.getTeacher().getId()
                                : null)
                .teacherName(
                        classEntity.getTeacher() != null
                                ? classEntity.getTeacher().getFullName()
                                : null)
                .studentCount(classEntity.getStudentCount())
                .build();
    }
}
