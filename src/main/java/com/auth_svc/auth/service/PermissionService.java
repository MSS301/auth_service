package com.auth_svc.auth.service;

import com.auth_svc.auth.dto.request.PermissionRequest;
import com.auth_svc.auth.dto.response.PermissionResponse;
import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permission);
}