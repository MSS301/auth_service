package com.auth_svc.auth.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassResponse {
    Integer id;
    Integer schoolId;
    String schoolName;
    String name;
    Integer grade;
    Integer teacherId;
    String teacherName;
    Integer studentCount;
}
