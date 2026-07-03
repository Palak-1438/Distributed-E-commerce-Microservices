package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressRequest;
import com.ecommerce.user.dto.AddressResponse;
import com.ecommerce.user.dto.UserProfileRequest;
import com.ecommerce.user.dto.UserProfileResponse;
import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.dto.UserMapper;
import com.ecommerce.user.repository.AddressRepository;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String email) {
        User user = getUser(email);
        return userMapper.userToProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(String email, UserProfileRequest request) {
        // If it's a first time login and profile doesn't exist, we create it.
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        user = userRepository.save(user);
        return userMapper.userToProfileResponse(user);
    }

    @Override
    @Transactional
    public void deleteAccount(String email) {
        User user = getUser(email);
        userRepository.delete(user);
        // Note: Realistically, an event should be fired here so the Auth Service can also delete the credentials.
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddress(String email) {
        User user = getUser(email);
        if (user.getAddress() == null) {
            return null; // Or throw a specific AddressNotFoundException
        }
        return userMapper.addressToAddressResponse(user.getAddress());
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(String email, AddressRequest request) {
        User user = getUser(email);

        Address address = user.getAddress();
        if (address == null) {
            address = new Address();
            address.setUser(user);
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());

        address = addressRepository.save(address);
        user.setAddress(address);

        return userMapper.addressToAddressResponse(address);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for email: " + email));
    }
}
