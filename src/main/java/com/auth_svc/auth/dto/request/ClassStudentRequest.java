package com.auth_svc.auth.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudentRequest {
    @NotNull(message = "Class ID is required")
    Integer classId;

    @NotNull(message = "Student ID is required")
    Integer studentId;
}
