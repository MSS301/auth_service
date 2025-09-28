package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 4, message = "USERNAME_INVALID")
    @NotBlank(message = "USERNAME_INVALID")
    String username;

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "EMAIL_IS_REQUIRED")
    String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    @NotBlank(message = "INVALID_PASSWORD")
    String password;
}


