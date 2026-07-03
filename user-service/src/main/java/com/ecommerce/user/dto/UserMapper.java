package com.ecommerce.user.dto;

import com.ecommerce.user.entity.Address;
import com.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserProfileResponse userToProfileResponse(User user);

    AddressResponse addressToAddressResponse(Address address);
}
