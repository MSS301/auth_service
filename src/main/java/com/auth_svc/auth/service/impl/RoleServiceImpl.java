package com.auth_svc.auth.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.dto.request.RoleRequest;
import com.auth_svc.auth.dto.response.RoleResponse;
import com.auth_svc.auth.entity.Role;
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

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
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
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
