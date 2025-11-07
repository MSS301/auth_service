package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassRequest {
    Integer schoolId;

    @Size(max = 100, message = "Class name must not exceed 100 characters")
    String name;

    Integer grade;

    @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
    String password;

    Integer teacherId;
}
