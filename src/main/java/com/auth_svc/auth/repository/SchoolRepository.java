package com.auth_svc.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    @Query("SELECT s FROM School s WHERE s.name LIKE %:name% AND s.deleted = false")
    List<School> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM School s WHERE s.name LIKE %:name% AND s.deleted = false")
    Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT s FROM School s WHERE s.id = :id AND s.deleted = false")
    Optional<School> findById(Integer id);

    @Query("SELECT s FROM School s WHERE s.deleted = false")
    Page<School> findAll(Pageable pageable);

    boolean existsByName(String name);
}
