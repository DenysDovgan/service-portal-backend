package com.colorlaboratory.serviceportalbackend.service.notification;

import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.service.notification.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;

    public void sendUserCreatedNotification(User user, String password) {
        log.info("Sending user created notification for user: {}...", user.getEmail());
        try {
            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "Welcome",
                    "user-created.html",
                    Map.of(
                            "firstName", user.getFirstName(),
                            "email", user.getEmail(),
                            "password", password,
                            "loginLink", "#"
                    )
            );
            log.info("User created notification sent successfully");
        } catch (MessagingException e) {
            log.error("Failed to send user created notification for user: {}.", user.getEmail(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordChangedNotification(UserDto user) {
    }
}
