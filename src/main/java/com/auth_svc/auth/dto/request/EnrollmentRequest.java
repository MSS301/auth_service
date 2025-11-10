package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentRequest {
    @NotNull(message = "Class ID is required")
    Integer classId;

    @NotNull(message = "Password is required")
    String password;

    // studentId will be extracted from JWT token
}
