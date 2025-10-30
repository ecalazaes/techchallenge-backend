package com.pos.techchallenge_backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CLIENTE")
@NoArgsConstructor
@SuperBuilder
public class Cliente extends Usuario {
}
