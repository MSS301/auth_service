package com.auth_svc.auth.service;

public interface EmailService {
    void sendVerificationEmail(String to, String username, String verificationToken);

    // Send a notification email when a user signs in
    void sendSignInEmail(String to, String username);
}
