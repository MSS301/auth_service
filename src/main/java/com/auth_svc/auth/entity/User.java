package com.auth_svc.auth.entity;

import java.time.LocalDateTime;
import java.util.Set;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "email_verified", nullable = false, columnDefinition = "boolean default false")
    boolean emailVerified;

    @Column(name = "verification_token")
    String verificationToken;

    @Column(name = "verification_token_expiry")
    LocalDateTime verificationTokenExpiry;

    @Column(name = "google_id")
    String googleId;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    AuthProvider authProvider;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", columnDefinition = "VARCHAR(255)"),
            inverseJoinColumns = @JoinColumn(name = "role_name", columnDefinition = "VARCHAR(255)"))
    Set<Role> roles;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    boolean deleted;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (authProvider == null) {
            authProvider = AuthProvider.LOCAL;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public enum AuthProvider {
        LOCAL,
        GOOGLE
    }
}
