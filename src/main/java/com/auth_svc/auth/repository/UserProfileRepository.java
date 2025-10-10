package com.auth_svc.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByAccountId(Integer accountId);

    List<UserProfile> findBySchoolId(Integer schoolId);

    List<UserProfile> findByRole(String role);

    List<UserProfile> findBySchoolIdAndRole(Integer schoolId, String role);

    boolean existsByAccountId(Integer accountId);
}
