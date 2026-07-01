package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.AuthRequest;
import com.ecommerce.auth.dto.AuthResponse;
import com.ecommerce.auth.dto.RegisterRequest;

public interface AuthService {
    void registerUser(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    AuthResponse refreshToken(String token);
    void validateToken(String token);
}
