package com.auth_svc.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.ClassStudent;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Integer> {
    List<ClassStudent> findByClassEntityId(Integer classId);

    List<ClassStudent> findByStudentId(Integer studentId);

    Optional<ClassStudent> findByClassEntityIdAndStudentId(Integer classId, Integer studentId);

    boolean existsByClassEntityIdAndStudentId(Integer classId, Integer studentId);

    @Query("SELECT cs FROM ClassStudent cs WHERE cs.classEntity.id = :classId")
    List<ClassStudent> findAllByClassId(Integer classId);

    void deleteByClassEntityIdAndStudentId(Integer classId, Integer studentId);
}
