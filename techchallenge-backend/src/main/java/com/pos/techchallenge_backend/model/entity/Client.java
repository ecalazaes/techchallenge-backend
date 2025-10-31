package com.pos.techchallenge_backend.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CLIENT")
@NoArgsConstructor
@SuperBuilder
public class Client extends User {

}
