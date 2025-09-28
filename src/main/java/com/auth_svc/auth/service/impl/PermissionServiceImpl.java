package com.auth_svc.auth.service.impl;

import com.auth_svc.auth.dto.request.PermissionRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;
import com.auth_svc.auth.entity.Permission;
import com.auth_svc.auth.repository.PermissionRepository;
import com.auth_svc.auth.service.PermissionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission = permissionRepository.save(permission);
        return toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

    private PermissionResponse toPermissionResponse(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        return response;
    }
}


