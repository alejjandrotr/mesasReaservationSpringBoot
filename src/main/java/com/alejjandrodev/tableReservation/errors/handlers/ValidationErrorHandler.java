package com.alejjandrodev.tableReservation.errors.handlers;

import com.alejjandrodev.tableReservation.errors.dtos.MessagesBadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessagesBadRequest> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(obj -> obj.getField() + ": " + obj.getDefaultMessage()).toList();
        String failObjectName = ex.getBindingResult().getObjectName();
        MessagesBadRequest msg = new MessagesBadRequest("Bad Request Error: please review data for " + failObjectName, fieldErrors);
        return ResponseEntity.badRequest().body(msg);
    }
}