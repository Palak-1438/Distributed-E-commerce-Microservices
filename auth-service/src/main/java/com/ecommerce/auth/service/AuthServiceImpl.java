package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.AuthRequest;
import com.ecommerce.auth.dto.AuthResponse;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.entity.UserCredential;
import com.ecommerce.auth.exception.InvalidCredentialsException;
import com.ecommerce.auth.exception.UserAlreadyExistsException;
import com.ecommerce.auth.repository.UserCredentialRepository;
import com.ecommerce.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerUser(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        UserCredential credential = UserCredential.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("CUSTOMER") // Hardcode to CUSTOMER for registration to prevent privilege escalation
                .build();

        repository.save(credential);

        // TODO: In the future, emit a Kafka event here (e.g. UserRegisteredEvent) to sync the profile in User Service
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            UserCredential user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid login request"));
            String accessToken = jwtService.generateToken(request.getEmail(), user.getRole());
            String refreshToken = jwtService.generateRefreshToken(request.getEmail());
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new InvalidCredentialsException("Invalid login request");
        }
    }

    @Override
    public AuthResponse refreshToken(String token) {
        try {
            jwtService.validateToken(token);
            String email = jwtService.extractUsername(token);
            UserCredential user = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
            String newAccessToken = jwtService.generateToken(email, user.getRole());
            String newRefreshToken = jwtService.generateRefreshToken(email);
            return new AuthResponse(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
