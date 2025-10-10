package com.auth_svc.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    List<School> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);
}
