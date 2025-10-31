package com.pos.techchallenge_backend.mapper;

import com.pos.techchallenge_backend.model.dto.AddressRequest;
import com.pos.techchallenge_backend.model.dto.AddressResponse;
import com.pos.techchallenge_backend.model.dto.user.UserRegistrationRequest;
import com.pos.techchallenge_backend.model.dto.user.UserResponse;
import com.pos.techchallenge_backend.model.entity.Address;
import com.pos.techchallenge_backend.model.entity.Client;
import com.pos.techchallenge_backend.model.entity.RestaurantOwner;
import com.pos.techchallenge_backend.model.entity.User;
import com.pos.techchallenge_backend.model.enums.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Address mapAddressRequestToAddress(AddressRequest request){
        if (request == null) return null;
        return Address.builder()
                .street(request.getStreet())
                .number(request.getNumber())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .build();
    }

    public AddressResponse mapAddressToAddressResponse(Address address){
        if (address == null) return null;
        AddressResponse response = new AddressResponse();
        response.setStreet(address.getStreet());
        response.setNumber(address.getNumber());
        response.setCity(address.getCity());
        response.setZipCode(address.getZipCode());
        return response;
    }

    public User mapRegistrationRequestToUser(UserRegistrationRequest request){
        User user;

        if (request.getUserType() == UserType.CLIENT){
            user = new Client();
        } else if (request.getUserType() == UserType.RESTAURANT_OWNER) {
            user = new RestaurantOwner();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(mapAddressRequestToAddress(request.getAddress()));
        user.setLastUpdateDate(LocalDateTime.now());

        return user;
    }

    public UserResponse mapUserToUserResponse(User user){
        if (user == null) return null;

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setLogin(user.getLogin());
        response.setLastUpdatedDate(user.getLastUpdateDate());
        response.setAddress(mapAddressToAddressResponse(user.getAddress()));

        if (user instanceof Client){
            response.setUserType(UserType.CLIENT);
        } else if (user instanceof RestaurantOwner) {
            response.setUserType(UserType.RESTAURANT_OWNER);
        }

        return response;
    }
}
