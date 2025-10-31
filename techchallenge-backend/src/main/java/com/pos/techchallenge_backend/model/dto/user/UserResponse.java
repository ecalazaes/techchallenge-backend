package com.pos.techchallenge_backend.model.dto.user;

import com.pos.techchallenge_backend.model.dto.AddressResponse;
import com.pos.techchallenge_backend.model.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String login;
    private UserType userType;
    private LocalDateTime lastUpdatedDate;
    private AddressResponse address;
}
