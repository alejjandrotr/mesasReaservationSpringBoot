package com.alejjandrodev.tableReservation.controllers;

import com.alejjandrodev.tableReservation.dtos.LoginDto;
import com.alejjandrodev.tableReservation.dtos.LoginResponse;
import com.alejjandrodev.tableReservation.dtos.RegisterDto;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.security.JWTUtil;
import com.alejjandrodev.tableReservation.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ServerUser> register(@Valid @RequestBody RegisterDto registerRequest) {
        ServerUser createdUser = authService.register(registerRequest);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginRequest) {
        ServerUser user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String jwtToken = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(jwtToken);
    }
}

