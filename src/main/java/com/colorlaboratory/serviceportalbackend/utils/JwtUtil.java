package com.colorlaboratory.serviceportalbackend.utils;

import com.colorlaboratory.serviceportalbackend.model.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "ffb91397c3b4a1efad4ee6fb5bcb8a8f18c446cd48c091184ac71ba75f5ad81816babdf60555a6435901ad2b1d89d33880b8b66d07eee02fb536b0abfad1aba54cb4b5edf251ecb989f997921ec91f36de9476666ce255daa66b34ed75ebb3c348c04d394c0678a65edf6e51ace77685594d37c64a96f6cefb4a0c5a80da0855f75974b2efa049370dda1766dbd9ae57ccf4fefdb5c1af7e2e1f5a01dd63813a798f7ff34c9fb653fb026346fcab7331ecfdd4b5f4fbc0158f0fef7a424d58ed5a7a3dec3568d8dda2c0843dc14b4ed8aecfc35109e25f7219db831244a7e89f90a896982ceffe56d5911adb8537478f4f6fbb2d1279029325aa10a974e1e168";

    private final Long EXPIRATION_TIME = 86400000L;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256)
                .compact();
    }
}
