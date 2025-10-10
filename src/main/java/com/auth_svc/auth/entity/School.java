package com.auth_svc.auth.entity;

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

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<UserProfile> userProfiles;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    List<Class> classes;
}
