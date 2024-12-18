package com.alejjandrodev.tableReservation.errors.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class NotAuthorizationHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Establecer el código de estado HTTP
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // Personalizar el mensaje de respuesta
        response.getWriter().write("Acceso denegado: no tienes permiso para acceder a este recurso.");
        response.getWriter().flush();
    }
}