package com.auth_svc.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.ClassStudentRequest;
import com.auth_svc.auth.dto.request.EnrollmentRequest;
import com.auth_svc.auth.dto.response.ClassStudentResponse;
import com.auth_svc.auth.entity.Class;
import com.auth_svc.auth.entity.ClassStudent;
import com.auth_svc.auth.entity.UserProfile;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.ClassRepository;
import com.auth_svc.auth.repository.ClassStudentRepository;
import com.auth_svc.auth.repository.UserProfileRepository;
import com.auth_svc.auth.service.ClassStudentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ClassStudentServiceImpl implements ClassStudentService {
    ClassStudentRepository classStudentRepository;
    ClassRepository classRepository;
    UserProfileRepository userProfileRepository;

    @Transactional
    @Override
    public ClassStudentResponse enrollStudent(ClassStudentRequest request) {
        log.info("Enrolling student {} to class {}", request.getStudentId(), request.getClassId());

        if (classStudentRepository.existsByClassEntityIdAndStudentId(request.getClassId(), request.getStudentId())) {
            throw new AppException(ErrorCode.CLASS_STUDENT_ALREADY_EXISTS);
        }

        Class classEntity = classRepository
                .findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        UserProfile student = userProfileRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        ClassStudent classStudent =
                ClassStudent.builder().classEntity(classEntity).student(student).build();

        classStudent = classStudentRepository.save(classStudent);

        // Increment student count
        classEntity.setStudentCount(classEntity.getStudentCount() + 1);
        classRepository.save(classEntity);

        return mapToResponse(classStudent);
    }

    @Transactional
    @Override
    public ClassStudentResponse enrollWithPassword(EnrollmentRequest request, String studentId) {
        log.info("Student {} requesting to enroll in class {} with password", studentId, request.getClassId());

        UserProfile student = userProfileRepository
                .findByAccountId(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        if (classStudentRepository.existsByClassEntityIdAndStudentId(request.getClassId(), student.getId())) {
            throw new AppException(ErrorCode.CLASS_STUDENT_ALREADY_EXISTS);
        }

        Class classEntity = classRepository
                .findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        // Verify password
        if (classEntity.getPassword() == null || !classEntity.getPassword().equals(request.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CLASS_PASSWORD);
        }

        ClassStudent classStudent =
                ClassStudent.builder().classEntity(classEntity).student(student).build();

        classStudent = classStudentRepository.save(classStudent);

        // Increment student count
        classEntity.setStudentCount(classEntity.getStudentCount() + 1);
        classRepository.save(classEntity);

        log.info("Student {} successfully enrolled in class {}", studentId, request.getClassId());
        return mapToResponse(classStudent);
    }

    @Override
    public ClassStudentResponse getEnrollmentById(Integer id) {
        log.info("Getting enrollment by id: {}", id);
        ClassStudent classStudent = classStudentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_STUDENT_NOT_FOUND));
        return mapToResponse(classStudent);
    }

    @Override
    public List<ClassStudentResponse> getMyEnrollments(String accountId) {
        log.info("Getting enrollments for student with accountId: {}", accountId);

        UserProfile student = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        return classStudentRepository.findByStudentId(student.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassStudentResponse> getAllEnrollments() {
        log.info("Getting all enrollments");
        return classStudentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassStudentResponse> getEnrollmentsByClass(Integer classId) {
        log.info("Getting enrollments by class ID: {}", classId);
        return classStudentRepository.findByClassEntityId(classId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassStudentResponse> getEnrollmentsByStudent(Integer studentId) {
        log.info("Getting enrollments by student ID: {}", studentId);
        return classStudentRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void unenrollStudent(Integer classId, Integer studentId) {
        log.info("Unenrolling student {} from class {}", studentId, classId);

        if (!classStudentRepository.existsByClassEntityIdAndStudentId(classId, studentId)) {
            throw new AppException(ErrorCode.CLASS_STUDENT_NOT_FOUND);
        }

        classStudentRepository.deleteByClassEntityIdAndStudentId(classId, studentId);

        // Decrement student count
        Class classEntity =
                classRepository.findById(classId).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        classEntity.setStudentCount(Math.max(0, classEntity.getStudentCount() - 1));
        classRepository.save(classEntity);
    }

    @Transactional
    @Override
    public void unenrollSelf(Integer classId, String accountId) {
        log.info("Student with accountId {} unenrolling from class {}", accountId, classId);

        UserProfile student = userProfileRepository
                .findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_PROFILE_NOT_FOUND));

        if (!classStudentRepository.existsByClassEntityIdAndStudentId(classId, student.getId())) {
            throw new AppException(ErrorCode.CLASS_STUDENT_NOT_FOUND);
        }

        classStudentRepository.deleteByClassEntityIdAndStudentId(classId, student.getId());

        // Decrement student count
        Class classEntity =
                classRepository.findById(classId).orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        classEntity.setStudentCount(Math.max(0, classEntity.getStudentCount() - 1));
        classRepository.save(classEntity);

        log.info("Student {} successfully unenrolled from class {}", accountId, classId);
    }

    @Transactional
    @Override
    public void deleteEnrollment(Integer id) {
        log.info("Deleting enrollment id: {}", id);
        if (!classStudentRepository.existsById(id)) {
            throw new AppException(ErrorCode.CLASS_STUDENT_NOT_FOUND);
        }
        classStudentRepository.deleteById(id);
    }

    private ClassStudentResponse mapToResponse(ClassStudent classStudent) {
        return ClassStudentResponse.builder()
                .id(classStudent.getId())
                .classId(
                        classStudent.getClassEntity() != null
                                ? classStudent.getClassEntity().getId()
                                : null)
                .className(
                        classStudent.getClassEntity() != null
                                ? classStudent.getClassEntity().getName()
                                : null)
                .studentId(
                        classStudent.getStudent() != null
                                ? classStudent.getStudent().getId()
                                : null)
                .studentName(
                        classStudent.getStudent() != null
                                ? classStudent.getStudent().getFullName()
                                : null)
                .build();
    }
}
