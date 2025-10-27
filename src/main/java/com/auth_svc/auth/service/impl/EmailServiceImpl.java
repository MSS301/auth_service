package com.auth_svc.auth.service.impl;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.auth_svc.auth.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailServiceImpl implements EmailService {

    ObjectProvider<JavaMailSender> mailSenderProvider;

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        String verificationLink = "http://localhost:8080/auth-service/auth/email-verification?token=" + verificationToken;

        try {
            JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
            if (mailSender == null) {
                log.warn("JavaMailSender bean not configured. Skipping email send. Verification link: {}", verificationLink);
                return;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = buildVerificationEmailHtml(username, verificationLink);

            helper.setTo(to);
            helper.setSubject("Verify Your Email - School Management System");
            helper.setText(htmlMsg, true); // true = HTML content

            mailSender.send(mimeMessage);
            log.info("Verification email sent to: {}", to);
        } catch (MessagingException ex) {
            log.error("Failed to send verification email to {}: {}", to, ex.getMessage());
            log.debug("Fallback verification link for {} -> {}", to, verificationLink);
        }
    }

    private String buildVerificationEmailHtml(String username, String verificationLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".button { display: inline-block; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 4px; margin: 20px 0; }" +
                ".button:hover { background-color: #0056b3; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2>Email Verification</h2>" +
                "<p>Hello " + username + ",</p>" +
                "<p>Thank you for registering! Please verify your email by clicking the button below:</p>" +
                "<a href='" + verificationLink + "' class='button'>Verify Email</a>" +
                "<p>Or copy and paste this link into your browser:</p>" +
                "<p><a href='" + verificationLink + "'>" + verificationLink + "</a></p>" +
                "<p>This link will expire in 24 hours.</p>" +
                "<p>If you didn't create this account, please ignore this email.</p>" +
                "<p>Best regards,<br>School Management Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    @Override
    public void sendSignInEmail(String to, String username) {
        try {
            JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
            if (mailSender == null) {
                log.warn("JavaMailSender bean not configured. Skipping sign-in email for {}", to);
                return;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = buildSignInEmailHtml(username);

            helper.setTo(to);
            helper.setSubject("New Sign-In Detected - School Management System");
            helper.setText(htmlMsg, true);

            mailSender.send(mimeMessage);
            log.info("Sign-in email sent to: {}", to);
        } catch (MessagingException ex) {
            log.error("Failed to send sign-in email to {}: {}", to, ex.getMessage());
        }
    }

    private String buildSignInEmailHtml(String username) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h2>New Sign-In Detected</h2>" +
                "<p>Hello " + (username == null ? "User" : username) + ",</p>" +
                "<p>This is a confirmation that a successful sign-in to your account just occurred.</p>" +
                "<p>If this was you, no action is needed. If you did not sign in, please reset your password immediately.</p>" +
                "<p>Best regards,<br>School Management Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}