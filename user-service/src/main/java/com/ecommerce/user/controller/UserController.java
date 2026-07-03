package com.ecommerce.user.controller;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.AddressResponse;
import com.ecommerce.user.dto.UserProfileRequest;
import com.ecommerce.user.dto.UserProfileResponse;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        String email = authentication.getName(); // Extracted from SecurityContext set by the filter
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@Valid @RequestBody UserProfileRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateProfile(email, request));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteAccount(email);
        return ResponseEntity.ok("Account deleted successfully.");
    }

    @GetMapping("/address")
    public ResponseEntity<AddressResponse> getAddress(Authentication authentication) {
        String email = authentication.getName();
        AddressResponse address = userService.getAddress(email);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.noContent().build();
    }

    @PutMapping("/address")
    public ResponseEntity<AddressResponse> updateAddress(@Valid @RequestBody AddressRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateAddress(email, request));
    }
}
