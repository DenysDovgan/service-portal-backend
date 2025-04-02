package com.colorlaboratory.serviceportalbackend.controller.user;

import com.colorlaboratory.serviceportalbackend.model.dto.api.responses.ApiResponse;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.ChangePasswordRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.CreateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.requests.UpdateUserRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.user.UserDto;
import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import com.colorlaboratory.serviceportalbackend.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ApiResponse<List<UserDto>>> filterUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) @PositiveOrZero Integer minAssignedIssues
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "Users filtered successfully",
                userService.filterUsers(role, sortBy, order, name, email, company, city, country, minAssignedIssues)
        ));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(
                "User retrieved successfully",
                userService.getCurrentUserDto()
        ));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(
            @PathVariable @NotNull @Positive Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "User retrieved successfully",
                userService.getUserDtoById(userId)
        ));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<UserDto>> createUser(
            @RequestBody @NotNull @Valid CreateUserRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "User created successfully",
                userService.createUser(request)
        ));
    }

    @PutMapping("/{userId}/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @PathVariable @NotNull @Positive Long userId,
            @RequestBody @NotNull @Valid ChangePasswordRequest request
    ) {
        userService.changePassword(request, userId);
        return ResponseEntity.ok(ApiResponse.success(
                "Password changed successfully",
                null
        ));
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable @NotNull @Positive Long userId,
            @RequestBody @NotNull @Valid UpdateUserRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                "User updated successfully",
                userService.updateUser(userId, request)
        ));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SERVICE_MANAGER')")
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @PathVariable @NotNull @Positive Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(
                "User deleted successfully",
                null
        ));
    }
}
