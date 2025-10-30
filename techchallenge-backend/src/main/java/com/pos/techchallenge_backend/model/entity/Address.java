package com.pos.techchallenge_backend.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Indica que esta classe será embutida em uma entidade
public class Address {
    private String street;
    private String number;
    private String city;
    private String zipCode;

}