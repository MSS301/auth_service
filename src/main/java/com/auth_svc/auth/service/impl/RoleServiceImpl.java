package com.auth_svc.auth.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.RoleRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;
import com.auth_svc.auth.dto.response.RoleResponse;
import com.auth_svc.auth.entity.Permission;
import com.auth_svc.auth.entity.Role;
import com.auth_svc.auth.repository.PermissionRepository;
import com.auth_svc.auth.repository.RoleRepository;
import com.auth_svc.auth.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(this::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String role) {
        roleRepository.deleteById(role);
    }

    private RoleResponse toRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        if (role.getPermissions() != null) {
            response.setPermissions(role.getPermissions().stream()
                    .map(this::toPermissionResponse)
                    .collect(Collectors.toSet()));
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
