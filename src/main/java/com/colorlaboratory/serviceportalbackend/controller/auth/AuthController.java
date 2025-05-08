package com.colorlaboratory.serviceportalbackend.controller.auth;

import com.colorlaboratory.serviceportalbackend.model.dto.auth.requests.LoginRequest;
import com.colorlaboratory.serviceportalbackend.model.dto.auth.responses.LoginResponse;
import com.colorlaboratory.serviceportalbackend.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    // TODO: 403 -> 401 if failed
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        log.info("Login request received for user with email {}", loginRequest.getEmail());
        return ResponseEntity.ok(
                new LoginResponse(authService.authenticate(loginRequest))
        );
    }
}
