package com.pos.techchallenge_backend.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(example = "{\n  \"currentPassword\": \"SenhaSegura123\",\n  \"newPassword\": \"NovaSenhaForte456\"\n}")
public class PasswordUpdateRequest {

    @NotBlank(message = "A senha atual é obrigatória")
    private String currentPassword;

    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
    @NotBlank(message = "A nova senha é obrigatória")
    private String newPassword;
}