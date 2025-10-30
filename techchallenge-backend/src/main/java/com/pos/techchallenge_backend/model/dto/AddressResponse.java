package com.pos.techchallenge_backend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private String street;
    private String number;
    private String city;
    private String zipCode;
}