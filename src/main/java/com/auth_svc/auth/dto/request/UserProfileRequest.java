package com.auth_svc.auth.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileRequest {
    @NotNull(message = "Account ID is required")
    String accountId;

    Integer schoolId;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    String fullName;

    LocalDate dateOfBirth;

    String avatarUrl;

    @Size(max = 50, message = "Role must not exceed 50 characters")
    String role;
}
