package com.pos.techchallenge_backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("DONO_RESTAURANTE")
@NoArgsConstructor
@SuperBuilder
public class DonoRestaurante extends Usuario {
}
