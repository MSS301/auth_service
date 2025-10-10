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
@Table(name = "classes")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    School school;

    @Column(name = "name", length = 100)
    String name;

    @Column(name = "grade")
    Integer grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    UserProfile teacher;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL)
    List<ClassStudent> classStudents;
}
