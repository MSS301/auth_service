package com.auth_svc.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.Class;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {
    List<Class> findBySchoolId(Integer schoolId);

    List<Class> findByTeacherId(Integer teacherId);

    List<Class> findBySchoolIdAndGrade(Integer schoolId, Integer grade);

    List<Class> findByGrade(Integer grade);
}
