package com.colorlaboratory.serviceportalbackend.controller.user;

import com.colorlaboratory.serviceportalbackend.model.dto.api.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.user.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UpdateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HEAD_MANAGER')")
    public ResponseEntity<List<UserDto>> getFilteredUsers(
            @RequestParam Role role,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) Integer minIssues,
            @RequestParam(required = false) Integer maxIssues,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(userService.getFilteredUsers(role, sortBy, order, minIssues, maxIssues, company, name));
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER', 'TECHNICIAN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER', 'TECHNICIAN')")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable @NotNull Long userId, @RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request, userId);
        return ResponseEntity.ok(new ApiResponse("Password changed successfully"));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable @NotNull Long userId, @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable @NotNull Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully"));
    }
}
