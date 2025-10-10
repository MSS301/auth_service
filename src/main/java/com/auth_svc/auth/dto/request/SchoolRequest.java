package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchoolRequest {
    @NotBlank(message = "School name is required")
    @Size(max = 255, message = "School name must not exceed 255 characters")
    String name;

    String address;
}
