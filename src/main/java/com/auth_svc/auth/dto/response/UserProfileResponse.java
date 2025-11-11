package com.auth_svc.auth.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponse {
    Integer id;
    String accountId;
    Integer schoolId;
    String schoolName;
    String fullName;
    LocalDate dateOfBirth;
    String avatarUrl;
    String role;
    String teacherProofUrl;
    boolean teacherProofVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
