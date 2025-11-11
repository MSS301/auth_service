package com.auth_svc.auth.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth_svc.auth.constant.PredefinedRole;
import com.auth_svc.auth.dto.request.UserCreationRequest;
import com.auth_svc.auth.dto.request.UserUpdateRequest;
import com.auth_svc.auth.dto.response.RoleResponse;
import com.auth_svc.auth.dto.response.UserResponse;
import com.auth_svc.auth.entity.Role;
import com.auth_svc.auth.entity.User;
import com.auth_svc.auth.exception.AppException;
import com.auth_svc.auth.exception.ErrorCode;
import com.auth_svc.auth.repository.RoleRepository;
import com.auth_svc.auth.repository.UserRepository;
import com.auth_svc.auth.service.EmailService;
import com.auth_svc.auth.service.UserService;
import com.auth_svc.event.UserEventProducer;
import com.auth_svc.event.UserRegisteredEvent;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserEventProducer userEventProducer;
    EmailService emailService;

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        user.setEmailVerified(false);
        user.setAuthProvider(User.AuthProvider.LOCAL);

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationToken);

        // Publish user.registered event to Kafka
        UserRegisteredEvent userRegisteredEvent = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .timestamp(LocalDateTime.now())
                .build();
        userEventProducer.publishUserRegisteredEvent(userRegisteredEvent);

        return toUserResponse(user);
    }

    //    @Override
    //    public UserResponse getMyInfo() {
    //        var context = SecurityContextHolder.getContext();
    //        String userId = context.getAuthentication().getName();
    //        User user = userRepository.findById(userId).orElseThrow(() -> new
    // AppException(ErrorCode.USER_NOT_EXISTED));
    //        return toUserResponse(user);
    //    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        updateUserFromRequest(user, request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }
        return toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.softDelete();
        userRepository.save(user);
    }

    //    @Override
    //    @PreAuthorize("hasRole('ADMIN')")
    //    public List<UserResponse> getUsers() {
    //        log.info("In method get Users");
    //        return userRepository.findAll().stream().map(this::toUserResponse).collect(Collectors.toList());
    //    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(Pageable pageable) {
        log.info("In method get Users with pagination");
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> searchUsersByEmail(String email, Pageable pageable) {
        log.info("Searching users by email: {} with pagination", email);
        return userRepository.findByEmailContainingIgnoreCase(email, pageable).map(this::toUserResponse);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> searchUsersByUsername(String username, Pageable pageable) {
        log.info("Searching users by username: {} with pagination", username);
        return userRepository
                .findByUsernameContainingIgnoreCase(username, pageable)
                .map(this::toUserResponse);
    }

    @Override
    public UserResponse getUser(String id) {
        return toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponse promoteToTeacher(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Ensure roles set exists
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Get or create TEACHER role
        Role teacherRole = roleRepository.findById(PredefinedRole.TEACHER_ROLE).orElseGet(() -> {
            Role r = new Role();
            r.setName(PredefinedRole.TEACHER_ROLE);
            r.setDescription("Teacher role");
            return roleRepository.save(r);
        });

        user.getRoles().add(teacherRole);
        user = userRepository.save(user);

        return toUserResponse(user);
    }

    private User toUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return user;
    }

    private void updateUserFromRequest(User user, UserUpdateRequest request) {
        if (request.getFirstName() != null || request.getLastName() != null) {
            String first = request.getFirstName() == null ? "" : request.getFirstName();
            String last = request.getLastName() == null ? "" : request.getLastName();
            user.setUsername((first + " " + last).trim());
        }
    }

    private UserResponse toUserResponse(User user) {
        Set<RoleResponse> roleResponses = user.getRoles() == null
                ? Set.of()
                : user.getRoles().stream().map(this::toRoleResponse).collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .avatarUrl(user.getAvatarUrl())
                .authProvider(user.getAuthProvider())
                .roles(roleResponses)
                .build();
    }

    private RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
