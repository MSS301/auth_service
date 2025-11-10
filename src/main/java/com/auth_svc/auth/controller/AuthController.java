package com.auth_svc.auth.controller;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.auth_svc.auth.dto.request.GoogleAuthRequest;
import com.auth_svc.auth.dto.response.AuthenticationResponse;
import com.auth_svc.auth.service.GoogleOAuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("authenticated", false, "message", "Not authenticated"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("email", principal.getAttribute("email"));
        response.put("name", principal.getAttribute("name"));
        response.put("picture", principal.getAttribute("picture"));
        response.put(
                "roles",
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<Void> success(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            // Redirect to frontend with error
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/login?error=auth_failed"))
                    .build();
        }

        try {
            // Extract Google user data
            String googleId = principal.getAttribute("sub");
            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            String picture = principal.getAttribute("picture");

            // Create Google auth request
            GoogleAuthRequest googleAuthRequest = GoogleAuthRequest.builder()
                    .googleId(googleId)
                    .email(email)
                    .name(name)
                    .picture(picture)
                    .build();

            // Authenticate with Google (auto-register if not exists) and get JWT token
            AuthenticationResponse authResponse = googleOAuthService.authenticateWithGoogle(googleAuthRequest);

            // Build redirect URL
            String redirectUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:3000/oauth2/callback")
                    .queryParam("token", authResponse.getToken())
                    .build()
                    .encode()
                    .toUriString();

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();

        } catch (Exception e) {
            // Redirect to frontend with error
            String errorUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:3000/login")
                    .queryParam("error", "authentication_failed")
                    .build()
                    .encode()
                    .toUriString();

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(errorUrl))
                    .build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(@AuthenticationPrincipal OAuth2User principal) {
        boolean isAuthenticated = principal != null;
        return ResponseEntity.ok(Map.of("authenticated", isAuthenticated));
    }
}
