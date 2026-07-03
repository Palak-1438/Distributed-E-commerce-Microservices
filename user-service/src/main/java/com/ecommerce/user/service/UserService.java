package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.AddressResponse;
import com.ecommerce.user.dto.UserProfileRequest;
import com.ecommerce.user.dto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String email);
    UserProfileResponse updateProfile(String email, UserProfileRequest request);
    void deleteAccount(String email);

    AddressResponse getAddress(String email);
    AddressResponse updateAddress(String email, AddressRequest request);
}
