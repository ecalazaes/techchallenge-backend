package com.pos.techchallenge_backend.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(example = "{\n  \"login\": \"joao.cliente\",\n  \"password\": \"SenhaSegura123\"\n}")
public class LoginRequest {

    @NotBlank(message = "Login é obrigatório")
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
