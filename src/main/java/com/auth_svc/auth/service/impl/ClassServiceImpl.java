package com.auth_svc.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.ClassRequest;
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

    @Override
    public ClassResponse getClassById(Integer id) {
        log.info("Getting class by id: {}", id);
        Class classEntity = classRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        return mapToResponse(classEntity);
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        log.info("Getting all classes");
        return classRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ClassResponse> getClassesBySchool(Integer schoolId) {
        log.info("Getting classes by school ID: {}", schoolId);
        return classRepository.findBySchoolId(schoolId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponse> getClassesByTeacher(Integer teacherId) {
        log.info("Getting classes by teacher ID: {}", teacherId);
        return classRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponse> getClassesByGrade(Integer grade) {
        log.info("Getting classes by grade: {}", grade);
        return classRepository.findByGrade(grade).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassResponse> getClassesBySchoolAndGrade(Integer schoolId, Integer grade) {
        log.info("Getting classes by school ID: {} and grade: {}", schoolId, grade);
        return classRepository.findBySchoolIdAndGrade(schoolId, grade).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

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
        if (!classRepository.existsById(id)) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }
        classRepository.deleteById(id);
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
                .studentCount(
                        classEntity.getClassStudents() != null
                                ? classEntity.getClassStudents().size()
                                : 0)
                .build();
    }
}
