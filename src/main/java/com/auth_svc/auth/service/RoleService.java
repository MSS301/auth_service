package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.RoleRequest;
import com.auth_svc.auth.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    void delete(String role);
}
