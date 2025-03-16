package com.colorlaboratory.serviceportalbackend.controller.user;

import com.colorlaboratory.serviceportalbackend.model.dto.api.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.UpdateUserRequest;
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
import java.util.Map;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<List<UserDto>> filterUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer minAssignedIssues
    ) {
        return ResponseEntity.ok(userService.filterUsers(role, sortBy, order, name, email, phoneNumber, company, city, country, minAssignedIssues));
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable @NotNull Long userId, @RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request, userId);
        return ResponseEntity.ok(ApiResponse.message("Password changed successfully"));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable @NotNull Long userId, @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable @NotNull Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.message("User deleted successfully"));
    }
}
