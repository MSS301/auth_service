package com.auth_svc.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    @Query("SELECT up FROM UserProfile up WHERE up.accountId = :accountId AND up.deleted = false")
    Optional<UserProfile> findByAccountId(String accountId);

    @Query("SELECT up FROM UserProfile up WHERE up.school.id = :schoolId AND up.deleted = false")
    List<UserProfile> findBySchoolId(Integer schoolId);

    @Query("SELECT up FROM UserProfile up WHERE up.school.id = :schoolId AND up.deleted = false")
    Page<UserProfile> findBySchoolId(Integer schoolId, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.role = :role AND up.deleted = false")
    List<UserProfile> findByRole(String role);

    @Query("SELECT up FROM UserProfile up WHERE up.role = :role AND up.deleted = false")
    Page<UserProfile> findByRole(String role, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.school.id = :schoolId AND up.role = :role AND up.deleted = false")
    List<UserProfile> findBySchoolIdAndRole(Integer schoolId, String role);

    @Query("SELECT up FROM UserProfile up WHERE up.school.id = :schoolId AND up.role = :role AND up.deleted = false")
    Page<UserProfile> findBySchoolIdAndRole(Integer schoolId, String role, Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.id = :id AND up.deleted = false")
    Optional<UserProfile> findById(Integer id);

    @Query("SELECT up FROM UserProfile up WHERE up.deleted = false")
    Page<UserProfile> findAll(Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.teacherProofUrl IS NOT NULL AND up.deleted = false")
    Page<UserProfile> findProfilesWithTeacherProof(Pageable pageable);

    @Query("SELECT up FROM UserProfile up WHERE up.teacherProofVerified = :teacherProofVerified AND up.deleted = false")
    Page<UserProfile> findByTeacherProofVerified(Boolean teacherProofVerified, Pageable pageable);

    boolean existsByAccountId(String accountId);
}
