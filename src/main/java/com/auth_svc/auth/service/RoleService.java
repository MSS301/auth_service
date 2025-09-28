package com.auth_svc.auth.service;

import com.auth_svc.auth.dto.request.RoleRequest;
import com.auth_svc.auth.dto.response.RoleResponse;
import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String role);
}
