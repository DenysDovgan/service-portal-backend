package com.colorlaboratory.serviceportalbackend.service.user;

import com.colorlaboratory.serviceportalbackend.mapper.user.UserMapper;
import com.colorlaboratory.serviceportalbackend.model.dto.user.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.dto.user.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.service.notification.NotificationService;
import com.colorlaboratory.serviceportalbackend.validator.user.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final NotificationService notificationService;

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());

        userValidator.validateCreateUser(request);

        String generatedPassword = generateRandomPassword();
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        User newUser = User.builder()
                .email(request.getEmail())
                .passwordHash(hashedPassword)
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .city(request.getCity())
                .country(request.getCountry())
                .companyName(request.getCompanyName())
                .role(request.getRole())
                .build();

        userRepository.save(newUser);

        notificationService.sendUserCreatedNotification(newUser, generatedPassword);

        log.info("Created new user with id: {}", newUser.getId());
        return userMapper.toDto(newUser);
    }

    public void changePassword(ChangePasswordRequest request, Long userId) {
        log.info("Initiating password change for user with id: {}", userId);

        User targetUser = userValidator.validateChangePassword(request, userId);
        targetUser.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(targetUser);

        // Send notification
        notificationService.sendPasswordChangedNotification(userMapper.toDto(targetUser));

        log.info("Password successfully changed for user with id: {}", userId);
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*!";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
}
