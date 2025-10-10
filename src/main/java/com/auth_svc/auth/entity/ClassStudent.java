package com.auth_svc.auth.entity;

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
@Table(name = "class_students")
public class ClassStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    Class classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    UserProfile student;
}
