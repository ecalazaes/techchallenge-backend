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

/**
 * @Component
 * Classe utilitária responsável por mapear (converter) objetos entre
 * a camada de Domínio/Persistência (Entidades) e a camada de Interface (DTOs).
 * * * Responsável por:
 * 1. Mapear DTOs de Requisição para Entidades (incluindo a lógica de Single Table Inheritance).
 * 2. Mapear Entidades para DTOs de Resposta (omitindo campos sensíveis como a senha).
 * @author Erick Calazães
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Converte o DTO de Requisição de Endereço para a Entidade Address.
     * @param request DTO AddressRequest.
     * @return Entidade Address.
     */
    public Address mapAddressRequestToAddress(AddressRequest request){
        if (request == null) return null;
        return Address.builder()
                .street(request.getStreet())
                .number(request.getNumber())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .build();
    }

    /**
     * Converte a Entidade Address para o DTO de Resposta de Endereço.
     * @param address Entidade Address.
     * @return DTO AddressResponse.
     */
    public AddressResponse mapAddressToAddressResponse(Address address){
        if (address == null) return null;
        AddressResponse response = new AddressResponse();
        response.setStreet(address.getStreet());
        response.setNumber(address.getNumber());
        response.setCity(address.getCity());
        response.setZipCode(address.getZipCode());
        return response;
    }

    // --- Mapeamento de Usuário (Request para Entidade) ---

    /**
     * Mapeia o DTO de Cadastro para a Entidade User, resolvendo o Single Table Inheritance.
     * 1. Instancia a subclasse correta (Client ou RestaurantOwner) baseada no DTO.
     * 2. Criptografa a senha usando o PasswordEncoder.
     * @param request DTO UserRegistrationRequest.
     * @return Entidade User (subclasse: Client ou RestaurantOwner).
     */
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

    // --- Mapeamento de Usuário (Entidade para Resposta) ---

    /**
     * Mapeia a Entidade User para o DTO de Resposta, excluindo a senha e
     * convertendo o tipo da subclasse de volta para o Enum/Tipo de Registro do DTO.
     * @param user Entidade User (subclasse).
     * @return DTO UserResponse.
     */
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
