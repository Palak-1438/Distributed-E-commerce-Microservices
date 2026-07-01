package com.ecommerce.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${ecommerce.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secret;

    @Value("${ecommerce.jwt.expiration-ms:86400000}")
    private long jwtExpiration;

    @Value("${ecommerce.jwt.refresh-expiration-ms:604800000}")
    private long refreshExpiration;

    public String generateToken(String userName, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userName, jwtExpiration);
    }

    public String generateRefreshToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, refreshExpiration);
    }

    private String createToken(Map<String, Object> claims, String userName, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public void validateToken(final String token) {
        Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
    }

    public String extractUsername(final String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
