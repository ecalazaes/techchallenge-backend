package com.pos.techchallenge_backend.model.dto.user;

import com.pos.techchallenge_backend.model.dto.AddressRequest;
import com.pos.techchallenge_backend.model.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(example = "{\n  \"name\": \"João da Silva\",\n  \"email\": \"joao.silva@teste.com\",\n  \"login\": \"joao.cliente\",\n  \"password\": \"SenhaSegura123\",\n  \"userType\": \"CLIENT ou RESTAURANT_OWNER\",\n  \"address\": {\n    \"street\": \"Rua das Flores\",\n    \"number\": \"100A\",\n    \"city\": \"São Paulo\",\n    \"zipCode\": \"01234567\"\n  }\n}")
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
    @Schema(description = "Tipo: CLIENT ou RESTAURANT_OWNER", example = "CLIENT")
    private UserType userType;

    @NotNull(message = "O endereço é obrigatório")
    private AddressRequest address;
}
