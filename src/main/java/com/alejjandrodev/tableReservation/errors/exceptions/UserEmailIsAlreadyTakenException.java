package com.alejjandrodev.tableReservation.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserEmailIsAlreadyTakenException extends ResponseStatusException {
    public UserEmailIsAlreadyTakenException(String email) {
        super(HttpStatus.BAD_REQUEST, "The email '" + email + "' is already taken. Please choose another one.");
    }
}
