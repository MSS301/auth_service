package com.auth_svc.auth.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.auth_svc.auth.dto.request.AuthenticationRequest;
import com.auth_svc.auth.dto.request.GoogleAuthRequest;
import com.auth_svc.auth.dto.request.IntrospectRequest;
import com.auth_svc.auth.dto.request.LogoutRequest;
import com.auth_svc.auth.dto.request.RefreshRequest;
import com.auth_svc.auth.dto.response.ApiResponse;
import com.auth_svc.auth.dto.response.AuthenticationResponse;
import com.auth_svc.auth.dto.response.IntrospectResponse;
import com.auth_svc.auth.service.AuthenticationService;
import com.auth_svc.auth.service.GoogleOAuthService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    GoogleOAuthService googleOAuthService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/google")
    ApiResponse<AuthenticationResponse> authenticateWithGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        var result = googleOAuthService.authenticateWithGoogle(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/email-verification")
    ApiResponse<Void> verifyEmail(@RequestParam("token") String token) {
        authenticationService.verifyEmail(token);
        return ApiResponse.<Void>builder().message("email verified").build();
    }

    @PostMapping("/resend-verification")
    ApiResponse<Void> resendVerification(@RequestParam String email) {
        authenticationService.resendVerificationEmail(email);
        return ApiResponse.<Void>builder().message("Verification email sent").build();
    }
}
