package com.auth_svc.auth.service;

import java.text.ParseException;

import com.auth_svc.auth.dto.request.AuthenticationRequest;
import com.auth_svc.auth.dto.request.IntrospectRequest;
import com.auth_svc.auth.dto.request.LogoutRequest;
import com.auth_svc.auth.dto.request.RefreshRequest;
import com.auth_svc.auth.dto.response.AuthenticationResponse;
import com.auth_svc.auth.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    void verifyEmail(String request);

    void resendVerificationEmail(String email);
}
