package com.pos.techchallenge_backend.model.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class AddressRequest {

    @NotBlank(message = "Rua é obrigatória")
    private String street;

    @NotBlank(message = "Número é obrigatório")
    private String number;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;
}