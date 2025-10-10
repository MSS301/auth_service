package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileUpdateRequest {
    Integer schoolId;

    @Size(max = 255, message = "Full name must not exceed 255 characters")
    String fullName;

    java.time.LocalDate dateOfBirth;

    String avatarUrl;

    @Size(max = 50, message = "Role must not exceed 50 characters")
    String role;
}
