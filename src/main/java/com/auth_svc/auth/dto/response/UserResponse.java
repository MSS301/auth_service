package com.auth_svc.auth.dto.response;

import java.util.Set;

import com.auth_svc.auth.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    boolean emailVerified;
    String avatarUrl;
    User.AuthProvider authProvider;
    Set<RoleResponse> roles;
}
