package com.pos.techchallenge_backend.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequest {

    @NotBlank(message = "A senha atual é obrigatória")
    private String currentPassword;

    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
    @NotBlank(message = "A nova senha é obrigatória")
    private String newPassword;
}