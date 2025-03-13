package com.colorlaboratory.serviceportalbackend.controller.auth;

import com.colorlaboratory.serviceportalbackend.model.dto.auth.LoginRequest;
import com.colorlaboratory.serviceportalbackend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest);
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }
}
