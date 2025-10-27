package com.auth_svc.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.CONFLICT),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1009, "Invalid email address", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1009, "Email is required", HttpStatus.BAD_REQUEST),
    SCHOOL_NOT_FOUND(1010, "School not found", HttpStatus.NOT_FOUND),
    SCHOOL_ALREADY_EXISTS(1011, "School with this name already exists", HttpStatus.CONFLICT),
    USER_PROFILE_NOT_FOUND(1012, "User profile not found", HttpStatus.NOT_FOUND),
    USER_PROFILE_ALREADY_EXISTS(1013, "User profile already exists for this account", HttpStatus.CONFLICT),
    CLASS_NOT_FOUND(1014, "Class not found", HttpStatus.NOT_FOUND),
    CLASS_STUDENT_NOT_FOUND(1015, "Class student enrollment not found", HttpStatus.NOT_FOUND),
    CLASS_STUDENT_ALREADY_EXISTS(1016, "Student is already enrolled in this class", HttpStatus.CONFLICT),
    EMAIL_NOT_VERIFIED(1017, "Email not verified", HttpStatus.FORBIDDEN),
    INVALID_VERIFICATION_TOKEN(1018, "Invalid or expired verification token", HttpStatus.BAD_REQUEST),
    GOOGLE_AUTH_FAILED(1019, "Google authentication failed", HttpStatus.UNAUTHORIZED),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
