package com.pos.techchallenge_backend.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("RESTAURANT_OWNER")
@NoArgsConstructor
@SuperBuilder
public class RestaurantOwner extends User {
}
