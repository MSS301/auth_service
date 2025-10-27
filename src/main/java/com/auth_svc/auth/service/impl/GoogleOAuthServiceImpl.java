package com.auth_svc.auth.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.auth_svc.auth.constant.PredefinedRole;
import com.auth_svc.auth.dto.request.GoogleAuthRequest;
import com.auth_svc.auth.dto.response.AuthenticationResponse;
import com.auth_svc.auth.entity.Role;
import com.auth_svc.auth.entity.User;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.RoleRepository;
import com.auth_svc.auth.repository.UserRepository;
import com.auth_svc.auth.service.GoogleOAuthService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long VALID_DURATION;

    @Override
    @Transactional
    public AuthenticationResponse authenticateWithGoogle(GoogleAuthRequest request) {
        String googleId = request.getGoogleId();
        String email = request.getEmail();
        String name = request.getName();
        String pictureUrl = request.getPicture();

        if (googleId == null || googleId.trim().isEmpty()) {
            throw new AppException(ErrorCode.GOOGLE_AUTH_FAILED);
        }

        log.info("Google OAuth attempt - Google ID: {}, Email: {}", googleId, email);

        // First, try to find user by Google ID
        User user = userRepository.findByGoogleId(googleId).orElse(null);

        if (user != null) {
            // User found by Google ID - update profile if needed
            log.info("User found by Google ID: {}", googleId);
            updateUserProfile(user, name, pictureUrl);
        } else {
            // Try to find by email
            user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                // User exists with this email but no Google ID
                log.info("Linking existing email account to Google: {}", email);

                // Link Google account to existing user
                user.setGoogleId(googleId);
                user.setAuthProvider(User.AuthProvider.GOOGLE);
                user.setEmailVerified(true);
                updateUserProfile(user, name, pictureUrl);
            } else {
                // Create new user
                log.info("Creating new user from Google auth: {}", email);
                user = createNewGoogleUser(googleId, email, name, pictureUrl);
            }
        }

        // Save user changes
        user = userRepository.save(user);

        // Generate JWT token
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .expiryTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .build();
    }

    private User createNewGoogleUser(String googleId, String email, String name, String pictureUrl) {
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        // Generate a random password that cannot be used for login
        String randomPassword = UUID.randomUUID().toString();

        return User.builder()
                .googleId(googleId)
                .email(email)
                .username(name != null ? name : email.split("@")[0])
                .avatarUrl(pictureUrl)
                .emailVerified(true)
                .authProvider(User.AuthProvider.GOOGLE)
                .password(passwordEncoder.encode(randomPassword))
                .roles(roles)
                .build();
    }

    private void updateUserProfile(User user, String name, String pictureUrl) {
        boolean updated = false;

        // Update username if it's missing
        if (user.getUsername() == null && name != null) {
            user.setUsername(name);
            updated = true;
        }

        // Update avatar if it's missing
        if (user.getAvatarUrl() == null && pictureUrl != null) {
            user.setAvatarUrl(pictureUrl);
            updated = true;
        }

        if (updated) {
            log.info("Updated user profile for: {}", user.getEmail());
        }
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("school.edu")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("email", user.getEmail())
                .claim("emailVerified", user.isEmailVerified())
                .claim("googleId", user.getGoogleId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add("ROLE_" + role.getName()));
        }

        return stringJoiner.toString();
    }
}
