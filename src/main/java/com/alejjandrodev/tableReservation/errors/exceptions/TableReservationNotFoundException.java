package com.alejjandrodev.tableReservation.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TableReservationNotFoundException extends ResponseStatusException {
    public TableReservationNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "The table with the "+ id + " not found");
    }
}
