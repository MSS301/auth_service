package com.auth_svc.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import com.auth_svc.auth.dto.request.PermissionRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;
import com.auth_svc.auth.entity.Permission;
import com.auth_svc.auth.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        // Set fields from request to entity
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        // Add other fields as needed

        permission = permissionRepository.save(permission);

        return toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

    private PermissionResponse toPermissionResponse(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setName(permission.getName());
        // Add other fields as needed
        return response;
    }
}