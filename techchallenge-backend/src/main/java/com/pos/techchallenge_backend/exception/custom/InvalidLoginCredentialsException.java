package com.pos.techchallenge_backend.exception.custom;

public class InvalidLoginCredentialsException extends RuntimeException {

    public InvalidLoginCredentialsException(String message) {
        super(message);
    }
}
