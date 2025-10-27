package com.auth_svc.auth.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth_svc.auth.constant.PredefinedRole;
import com.auth_svc.auth.entity.Role;
import com.auth_svc.auth.entity.User;
import com.auth_svc.auth.repository.RoleRepository;
import com.auth_svc.auth.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin@school.edu";

    @NonFinal
    static final String ADMIN_PASSWORD = "Admin@123";

    @NonFinal
    static final String TEACHER_EMAIL = "teacher@school.edu";

    @NonFinal
    static final String TEACHER_PASSWORD = "Teacher@123";

    @NonFinal
    static final String USER_EMAIL = "user@school.edu";

    @NonFinal
    static final String USER_PASSWORD = "User@123";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            name = "driver-class-name",  // Changed from driverClassName
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            log.info("========================================");
            log.info("Starting application initialization...");
            log.info("========================================");

            try {
                // Create roles if they don't exist
                Role userRole = roleRepository.findById(PredefinedRole.USER_ROLE)
                        .orElseGet(() -> {
                            log.info("Creating USER role");
                            return roleRepository.save(Role.builder()
                                    .name(PredefinedRole.USER_ROLE)
                                    .description("User role - basic access")
                                    .build());
                        });
                log.info("✓ USER role ready");

                Role teacherRole = roleRepository.findById(PredefinedRole.TEACHER_ROLE)
                        .orElseGet(() -> {
                            log.info("Creating TEACHER role");
                            return roleRepository.save(Role.builder()
                                    .name(PredefinedRole.TEACHER_ROLE)
                                    .description("Teacher role - can manage classes")
                                    .build());
                        });
                log.info("✓ TEACHER role ready");

                Role adminRole = roleRepository.findById(PredefinedRole.ADMIN_ROLE)
                        .orElseGet(() -> {
                            log.info("Creating ADMIN role");
                            return roleRepository.save(Role.builder()
                                    .name(PredefinedRole.ADMIN_ROLE)
                                    .description("Admin role - full access")
                                    .build());
                        });
                log.info("✓ ADMIN role ready");

                // Create admin account
                if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                    var adminRoles = new HashSet<Role>();
                    adminRoles.add(adminRole);

                    User admin = User.builder()
                            .username("Admin User")
                            .email(ADMIN_EMAIL)
                            .emailVerified(true)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .roles(adminRoles)
                            .authProvider(User.AuthProvider.LOCAL)
                            .build();

                    userRepository.save(admin);
                    log.warn("✓ Admin account created: {} / {}", ADMIN_EMAIL, ADMIN_PASSWORD);
                } else {
                    log.info("✓ Admin account already exists");
                }

                // Create teacher account
                if (userRepository.findByEmail(TEACHER_EMAIL).isEmpty()) {
                    var teacherRoles = new HashSet<Role>();
                    teacherRoles.add(teacherRole);

                    User teacher = User.builder()
                            .username("Teacher User")
                            .email(TEACHER_EMAIL)
                            .emailVerified(true)
                            .password(passwordEncoder.encode(TEACHER_PASSWORD))
                            .roles(teacherRoles)
                            .authProvider(User.AuthProvider.LOCAL)
                            .build();

                    userRepository.save(teacher);
                    log.warn("✓ Teacher account created: {} / {}", TEACHER_EMAIL, TEACHER_PASSWORD);
                } else {
                    log.info("✓ Teacher account already exists");
                }

                // Create user account
                if (userRepository.findByEmail(USER_EMAIL).isEmpty()) {
                    var userRoles = new HashSet<Role>();
                    userRoles.add(userRole);

                    User user = User.builder()
                            .username("Regular User")
                            .email(USER_EMAIL)
                            .emailVerified(true)
                            .password(passwordEncoder.encode(USER_PASSWORD))
                            .roles(userRoles)
                            .authProvider(User.AuthProvider.LOCAL)
                            .build();

                    userRepository.save(user);
                    log.warn("✓ User account created: {} / {}", USER_EMAIL, USER_PASSWORD);
                } else {
                    log.info("✓ User account already exists");
                }

                log.info("========================================");
                log.info("Application initialization completed!");
                log.info("========================================");

            } catch (Exception e) {
                log.error("Error during application initialization", e);
                throw e;
            }
        };
    }
}