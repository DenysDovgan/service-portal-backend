package com.colorlaboratory.serviceportalbackend.service.auth;

import com.colorlaboratory.serviceportalbackend.model.dto.auth.requests.LoginRequest;
import com.colorlaboratory.serviceportalbackend.model.entity.user.User;
import com.colorlaboratory.serviceportalbackend.repository.user.UserRepository;
import com.colorlaboratory.serviceportalbackend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("User with email {} not found while trying to log in", request.getEmail());
                    return new BadCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("User with email {} entered wrong password while trying to log in", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        log.info("User with id: {} logged in successfully", user.getId());
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
