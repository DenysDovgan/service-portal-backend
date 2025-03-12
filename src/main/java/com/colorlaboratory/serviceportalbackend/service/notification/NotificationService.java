package com.colorlaboratory.serviceportalbackend.service.notification;

import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    public void sendUserCreatedNotification(User user, String password) {
        log.info("Sending user created notification for user: {}...", user.getId());

    }

    public void sendPasswordChangedNotification(UserDto user) {
    }
}
