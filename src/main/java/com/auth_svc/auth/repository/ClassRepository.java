package com.auth_svc.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    @Query("SELECT c FROM Class c WHERE c.school.id = :schoolId AND c.deleted = false")
    List<Class> findBySchoolId(Integer schoolId);

    @Query("SELECT c FROM Class c WHERE c.school.id = :schoolId AND c.deleted = false")
    Page<Class> findBySchoolId(Integer schoolId, Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId AND c.deleted = false")
    List<Class> findByTeacherId(Integer teacherId);

    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId AND c.deleted = false")
    Page<Class> findByTeacherId(Integer teacherId, Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.school.id = :schoolId AND c.grade = :grade AND c.deleted = false")
    List<Class> findBySchoolIdAndGrade(Integer schoolId, Integer grade);

    @Query("SELECT c FROM Class c WHERE c.school.id = :schoolId AND c.grade = :grade AND c.deleted = false")
    Page<Class> findBySchoolIdAndGrade(Integer schoolId, Integer grade, Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.grade = :grade AND c.deleted = false")
    List<Class> findByGrade(Integer grade);

    @Query("SELECT c FROM Class c WHERE c.grade = :grade AND c.deleted = false")
    Page<Class> findByGrade(Integer grade, Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.name LIKE %:name% AND c.deleted = false")
    Page<Class> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM Class c WHERE c.school.id = :schoolId AND c.name LIKE %:name% AND c.deleted = false")
    List<Class> findBySchoolIdAndNameContainingIgnoreCase(Integer schoolId, String name);

    @Query("SELECT c FROM Class c WHERE c.id = :id AND c.deleted = false")
    Optional<Class> findById(Integer id);

    @Query("SELECT c FROM Class c WHERE c.deleted = false")
    Page<Class> findAll(Pageable pageable);
}
