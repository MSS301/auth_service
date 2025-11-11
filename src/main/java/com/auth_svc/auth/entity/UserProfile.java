package com.auth_svc.auth.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "account_id", nullable = false, length = 255)
    String accountId; // link to Auth service User table (UUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    School school;

    @Column(name = "full_name", nullable = false, length = 255)
    String fullName;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    String avatarUrl;

    @Column(name = "teacher_proof_url", columnDefinition = "TEXT")
    String teacherProofUrl;

    @Column(name = "teacher_proof_verified", nullable = false, columnDefinition = "boolean default false")
    boolean teacherProofVerified;

    @Column(name = "role", length = 50)
    String role;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    boolean deleted;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    List<Class> teachingClasses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    List<ClassStudent> classStudents;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
