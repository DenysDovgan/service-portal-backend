package com.colorlaboratory.serviceportalbackend.validator.user;

import com.colorlaboratory.serviceportalbackend.mapper.user.UserMapper;
import com.colorlaboratory.serviceportalbackend.model.dto.user.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto validateGetCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch current authenticated user
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    log.error("Current user {} not found", currentUserEmail);
                    return new EntityNotFoundException("Current user " + currentUserEmail + " not found");
                });

        log.info("Fetched current user: {}", currentUserEmail);
        return userMapper.toDto(currentUser);
    }

    public void validateGetUserById(UserDto targetUser) {
        // Fetch current authenticated user
        UserDto currentUser = userService.getCurrentUser();

        // Admins can fetch any user, no further validation needed
        if (currentUser.getRole() == Role.ADMIN) {
            log.info("Admin {} fetched user: {}", currentUser.getId(), targetUser.getId());
            return;
        }

        if (targetUser.getRole() == Role.ADMIN) {
            log.warn("User {} with role {} tried to fetch user {} with role {}", currentUser.getId(), currentUser.getRole(), targetUser.getId(), targetUser.getRole());
            throw new AccessDeniedException("You are not allowed to fetch data of Admin users.");
        }

        log.info("User {} successfully validated for data fetch of user {}", currentUser.getId(), targetUser.getId());
    }

    public void validateCreateUser(CreateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if current logged-in user exists
        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("Current user {} not found while trying to create a new user", authentication.getName());
                    return new EntityNotFoundException("Current user " + authentication.getName() + " not found");
                });

        // Check if current User can create new User with specific roles
        if (currentUser.getRole() == Role.TECHNICIAN && request.getRole() != Role.CLIENT) {
            log.warn("User {} with role {} tried to create user {} with role {}", currentUser.getEmail(), currentUser.getRole(), request.getEmail(), request.getRole());
            throw new AccessDeniedException("Technicians can only create CLIENT users.");
        }

        if (currentUser.getRole() == Role.SERVICE_MANAGER && !(request.getRole() == Role.CLIENT || request.getRole() == Role.TECHNICIAN)) {
            log.warn("User {} with role {} tried to create user {} with role {}", currentUser.getEmail(), currentUser.getRole(), request.getEmail(), request.getRole());
            throw new AccessDeniedException("Service Managers can only create CLIENT or TECHNICIAN users.");
        }

        // Check if user is already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.error("Error while trying to create a new user: User with email {} already exists", request.getEmail());
            throw new EntityExistsException("User with email " + request.getEmail() + " already exists");
        }
    }

    public User validateChangePassword(ChangePasswordRequest request, Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch current authenticated user
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> {
                    log.error("Current user {} not found while trying to change password for user: {}", currentUserEmail, userId);
                    return new EntityNotFoundException("Current user " + currentUserEmail + " not found");
                });

        // Fetch target user (the one whose password is being changed)
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Error while trying to change password for user: {}. User not found", userId);
                    return new EntityNotFoundException("User with id " + userId + " not found");
                });

        // Admins can change any user's password, no further validation needed
        if (currentUser.getRole() == Role.ADMIN && targetUser.getRole() != Role.ADMIN) {
            log.info("Admin {} is changing password for user {}", currentUserEmail, userId);
            return targetUser;
        }

        // Ensure the user is changing their own password
        if (!currentUser.getId().equals(targetUser.getId())) {
            log.warn("Unauthorized password change attempt by {} for user {}", currentUserEmail, userId);
            throw new AccessDeniedException("You can only change your own password.");
        }

        // Validate the old password
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPasswordHash())) {
            log.warn("Incorrect old password attempt by {}", currentUserEmail);
            throw new AccessDeniedException("Old password is incorrect.");
        }

        log.info("User {} successfully validated for password change", currentUserEmail);
        return targetUser;
    }

    public void validateDeleteUser(@NotNull Long userId) {
        UserDto targetUser = userService.getUserById(userId);
        UserDto currentUser = userService.getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN) {
            log.info("Admin {} is deleting user {}", currentUser.getId(), targetUser.getId());
            return;
        }

        if (currentUser.getRole() == Role.SERVICE_MANAGER && (targetUser.getRole() != Role.CLIENT && targetUser.getRole() != Role.TECHNICIAN)) {
            log.warn("Service Manager {} tried to delete user {} with role {}", currentUser.getId(), targetUser.getId(), targetUser.getRole());
            throw new AccessDeniedException("Service Managers can only delete CLIENT or TECHNICIAN users.");
        }
    }

    public void validateUpdateUser(UserDto targetUser) {
        UserDto currentUser = userService.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) {
            log.info("Admin {} is updating user {}", currentUser.getId(), targetUser.getId());
            return;
        }

        if (currentUser.getRole() == Role.SERVICE_MANAGER && (targetUser.getRole() != Role.CLIENT && targetUser.getRole() != Role.TECHNICIAN)) {
            log.warn("Service Manager {} tried to update user {} with role {}", currentUser.getId(), targetUser.getId(), targetUser.getRole());
            throw new AccessDeniedException("Service Managers can only update CLIENT or TECHNICIAN users.");
        }

        if (currentUser.getRole() == Role.TECHNICIAN && targetUser.getRole() != Role.CLIENT) {
            log.warn("Technician {} tried to update user {} with role {}", currentUser.getId(), targetUser.getId(), targetUser.getRole());
            throw new AccessDeniedException("Technicians can only update CLIENT users.");
        }
    }

    public void validateGetFilteredUsers(Role role) {
        UserDto currentUser = userService.getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) {
            log.info("Admin {} is fetching users with role {}", currentUser.getId(), role);
            return;
        }

        if (role == Role.ADMIN) {
            log.warn("User {} with role {} tried to fetch users with role {}", currentUser.getId(), currentUser.getRole(), role);
            throw new AccessDeniedException("You are not allowed to fetch data of Admin users.");
        }
    }
}
