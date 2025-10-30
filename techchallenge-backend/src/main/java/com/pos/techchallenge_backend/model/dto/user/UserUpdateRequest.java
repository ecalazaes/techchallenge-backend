package com.pos.techchallenge_backend.model.dto.user;

import com.pos.techchallenge_backend.model.dto.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    // Note: Senha e UserType são excluídos para serem atualizados separadamente.

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "Login é obrigatório")
    private String login;

    private AddressRequest address; // Endereço pode ser opcional ou revalidado
}
