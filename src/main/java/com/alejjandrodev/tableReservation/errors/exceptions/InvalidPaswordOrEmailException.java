package com.alejjandrodev.tableReservation.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPaswordOrEmailException extends ResponseStatusException {
    public InvalidPaswordOrEmailException() {
        super(HttpStatus.UNAUTHORIZED,"Invalid email or password. Please check your credentials and try again.");
    }
}
