package com.colorlaboratory.serviceportalbackend.utils;

import com.colorlaboratory.serviceportalbackend.model.entity.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration-time}")
    private Long EXPIRATION_TIME;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (SECRET == null || SECRET.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long");
        }
        this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }


}
