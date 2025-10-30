package com.auth_svc.auth.entity;

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
@Table(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    boolean deleted;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<UserProfile> userProfiles;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<Class> classes;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
