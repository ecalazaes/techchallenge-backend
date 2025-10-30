package com.pos.techchallenge_backend.model.dto.user;

import com.pos.techchallenge_backend.model.dto.AddressRequest;
import com.pos.techchallenge_backend.model.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "Login é obrigatório")
    private String login;

    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @NotBlank(message = "Senha é obrigatória")
    private String password;

    @NotNull(message = "O tipo de usuário é obrigatório")
    private UserType userType;

    @NotNull(message = "O endereço é obrigatório")
    private AddressRequest address;
}
