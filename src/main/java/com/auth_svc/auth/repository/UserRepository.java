package com.auth_svc.auth.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.auth_svc.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted = false")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.googleId = :googleId AND u.deleted = false")
    Optional<User> findByGoogleId(String googleId);

    @Query("SELECT u FROM User u WHERE u.verificationToken = :token AND u.deleted = false")
    Optional<User> findByVerificationToken(String token);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findById(String id);

    // Pagination and search
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:email% AND u.deleted = false")
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username% AND u.deleted = false")
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
