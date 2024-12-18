package com.alejjandrodev.tableReservation.errors.dtos;

import java.util.List;

public class MessagesBadRequest extends Error {
    public List<String> errors;

    public MessagesBadRequest(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}