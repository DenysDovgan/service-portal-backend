package com.colorlaboratory.serviceportalbackend.service.user;

import com.colorlaboratory.serviceportalbackend.mapper.user.UserMapper;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.UpdateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.service.notification.NotificationService;
import com.colorlaboratory.serviceportalbackend.validator.user.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final NotificationService notificationService;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    public List<UserDto> getAllAdmins() {
        List<User> admins = userRepository.findByRole((Role.ADMIN));
        return userMapper.toDtoList(admins);
    }

    public List<UserDto> filterUsers(Role role, String sortBy, String order, String name, String email, String phoneNumber,
                                     String company, String city, String country, Integer minAssignedIssues) {
        UserDto currentUser = getCurrentUser();
        userValidator.validateGetFilteredUsers(currentUser, role);
        List<User> users = userRepository.filterUsers(role, sortBy, order, name, email, phoneNumber, company, city, country, minAssignedIssues);
        return userMapper.toDtoList(users);
    }

    public UserDto getCurrentUser() {
        return userValidator.validateGetCurrentUser();
    }

    public UserDto getUserById(@NotNull Long userId) {
        UserDto targetUser = userMapper.toDto(userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new EntityNotFoundException("User with id " + userId + " not found");
                }));

        userValidator.validateGetUserById(targetUser);

        return targetUser;
    }

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

    @Transactional
    public void changePassword(ChangePasswordRequest request, Long userId) {
        log.info("Initiating password change for user with id: {}", userId);

        User targetUser = userValidator.validateChangePassword(request, userId);
        targetUser.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(targetUser);

        // Send notification
        notificationService.sendPasswordChangedNotification(userMapper.toDto(targetUser));

        log.info("Password successfully changed for user with id: {}", userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);

        UserDto currentUser = getCurrentUser();
        UserDto targetUser = getUserById(userId);

        userValidator.validateDeleteUser(targetUser, currentUser);

        userRepository.deleteById(userId);

        log.info("User with id {} deleted successfully", userId);
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

    @Transactional
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with id: {}", userId);

        User targetUser = getUserEntityById(userId);
        UserDto currentUser = getCurrentUser();

        userValidator.validateUpdateUser(currentUser, userMapper.toDto(targetUser));

        targetUser.setEmail(request.getEmail());
        targetUser.setPhoneNumber(request.getPhoneNumber());
        targetUser.setCity(request.getCity());
        targetUser.setCountry(request.getCountry());
        targetUser.setCompanyName(request.getCompanyName());

        User updatedUser = userRepository.save(targetUser);

        log.info("User with id {} updated successfully", userId);
        return userMapper.toDto(updatedUser);
    }

    private User getUserEntityById(@NotNull Long userId) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new EntityNotFoundException("User with id " + userId + " not found");
                });

        userValidator.validateGetUserById(userMapper.toDto(targetUser));

        return targetUser;
    }
}
