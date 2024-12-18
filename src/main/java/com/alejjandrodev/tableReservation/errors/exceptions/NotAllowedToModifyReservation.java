package com.alejjandrodev.tableReservation.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAllowedToModifyReservation extends ResponseStatusException {
    public NotAllowedToModifyReservation(Long id) {
        super(HttpStatus.UNAUTHORIZED,"You are not allowed to modify this reservation with id: " + id);
    }
}
