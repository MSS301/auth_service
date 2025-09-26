package com.auth_svc.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.auth_svc.auth.dto.request.RoleRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;
import com.auth_svc.auth.dto.response.RoleResponse;
import com.auth_svc.auth.entity.Role;
import com.auth_svc.auth.entity.Permission;
import com.auth_svc.auth.repository.PermissionRepository;
import com.auth_svc.auth.repository.RoleRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }

    private RoleResponse toRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        if (role.getPermissions() != null) {
            response.setPermissions(
                    role.getPermissions().stream()
                            .map(this::toPermissionResponse)
                            .collect(Collectors.toSet())
            );
        }
        return response;
    }
    private PermissionResponse toPermissionResponse(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        return response;
    }
}
