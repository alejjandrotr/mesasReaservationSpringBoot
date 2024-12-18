package com.alejjandrodev.tableReservation.services;

import com.alejjandrodev.tableReservation.dtos.RegisterDto;
import com.alejjandrodev.tableReservation.errors.exceptions.InvalidPaswordOrEmailException;
import com.alejjandrodev.tableReservation.errors.exceptions.UserEmailIsAlreadyTakenException;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.repositories.ServerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;




import jakarta.validation.Valid;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ServerUserRepository serverUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private  AuthenticationManager authenticationManager;



    public ServerUser register(@Valid RegisterDto registerDto) {
        if (serverUserRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new UserEmailIsAlreadyTakenException(registerDto.getEmail());
        }

        ServerUser serverUser =  new ServerUser();
        serverUser.setName(registerDto.getName());
        serverUser.setPassword(registerDto.getPassword());
        serverUser.setEmail(registerDto.getEmail());

        serverUser.setPassword(passwordEncoder.encode(serverUser.getPassword()));

        return serverUserRepository.save(serverUser);
    }

    public ServerUser login(String email, String password) {
        Optional<ServerUser> userOptional = serverUserRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new InvalidPaswordOrEmailException();
        }
        ServerUser user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPaswordOrEmailException();

        }

        return user;

    }
}
