package com.auth_svc.auth.service;

import java.util.List;

import com.auth_svc.auth.dto.request.PermissionRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    void delete(String permission);
}
