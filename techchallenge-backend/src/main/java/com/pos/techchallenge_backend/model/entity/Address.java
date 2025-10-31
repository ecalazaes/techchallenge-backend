package com.pos.techchallenge_backend.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable // Indica que esta classe será embutida em uma entidade
public class Address {
    private String street;
    private String number;
    private String city;
    private String zipCode;

}