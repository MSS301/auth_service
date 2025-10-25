package com.auth_svc.auth.service;

import com.auth_svc.auth.dto.request.GoogleAuthRequest;
import com.auth_svc.auth.dto.response.AuthenticationResponse;

public interface GoogleOAuthService {
    AuthenticationResponse authenticateWithGoogle(GoogleAuthRequest request);
}
