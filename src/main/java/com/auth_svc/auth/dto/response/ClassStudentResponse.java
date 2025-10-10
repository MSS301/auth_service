package com.auth_svc.auth.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassStudentResponse {
    Integer id;
    Integer classId;
    String className;
    Integer studentId;
    String studentName;
}
