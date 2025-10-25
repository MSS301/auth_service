package com.auth_svc.auth.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

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
    public ResponseEntity<Map<String, Object>> success(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Authentication failed"));
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("google_id", principal.getAttribute("sub"));
        userDetails.put("email", principal.getAttribute("email"));
        userDetails.put("name", principal.getAttribute("name"));
        userDetails.put("picture", principal.getAttribute("picture"));
        userDetails.put(
                "roles",
                principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("user", userDetails);
        response.put("timestamp", new Date());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(@AuthenticationPrincipal OAuth2User principal) {
        boolean isAuthenticated = principal != null;
        return ResponseEntity.ok(Map.of("authenticated", isAuthenticated));
    }
}
