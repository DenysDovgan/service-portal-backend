package com.colorlaboratory.serviceportalbackend.controller.user;

import com.colorlaboratory.serviceportalbackend.model.dto.user.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_MANAGER', 'TECHNICIAN')")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request, userId);
        return ResponseEntity.ok("Password changed successfully.");
    }
}
