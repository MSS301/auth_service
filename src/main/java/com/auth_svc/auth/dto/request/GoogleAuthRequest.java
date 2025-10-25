package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleAuthRequest {
    @NotBlank(message = "Google ID is required")
    String googleId;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email;

    String name;

    String picture;
}
