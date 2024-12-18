package com.alejjandrodev.tableReservation.services;

import com.alejjandrodev.tableReservation.dtos.RegisterDto;
import com.alejjandrodev.tableReservation.errors.exceptions.InvalidPaswordOrEmailException;
import com.alejjandrodev.tableReservation.errors.exceptions.UserEmailIsAlreadyTakenException;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.repositories.ServerUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private ServerUserRepository serverUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ServerUser serverUser;
    private RegisterDto registerDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerDto = new RegisterDto();
        registerDto.setName("Alejandro Marcano");
        registerDto.setEmail("alejjandroe@etest.com");
        registerDto.setPassword("password123");

        serverUser = new ServerUser();
        serverUser.setName(registerDto.getName());
        serverUser.setEmail(registerDto.getEmail());
        serverUser.setPassword(registerDto.getPassword());


    }

    @Test
    public void testRegister_Success() {
        when(serverUserRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(serverUserRepository.save(any(ServerUser.class))).thenReturn(serverUser);

        ServerUser createdUser = authService.register(registerDto);

        assertNotNull(createdUser);
        assertEquals(serverUser.getName(), createdUser.getName());
        assertEquals(serverUser.getEmail(), createdUser.getEmail());
        verify(serverUserRepository, times(1)).save(any(ServerUser.class));
    }

    @Test
    public void testRegister_EmailAlreadyTaken() {
        when(serverUserRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(new ServerUser()));

        Exception exception = assertThrows(UserEmailIsAlreadyTakenException.class, () -> {
            authService.register(registerDto);
        });

        assertEquals((new UserEmailIsAlreadyTakenException(registerDto.getEmail())).getMessage(), exception.getMessage());
    }



    @Test
    public void testLogin_UserNotFound() {
        when(serverUserRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidPaswordOrEmailException.class, () -> {
            authService.login("nonexistent@example.com", "password123");
        });

        assertEquals((new InvalidPaswordOrEmailException()).getMessage(), exception.getMessage());
    }

    @Test
    public void testLogin_InvalidPassword() {
        when(serverUserRepository.findByEmail(serverUser.getEmail())).thenReturn(Optional.of(serverUser));
        when(passwordEncoder.matches("wrongPassword", serverUser.getPassword())).thenReturn(false);

        Exception exception = assertThrows(InvalidPaswordOrEmailException.class, () -> {
            authService.login(serverUser.getEmail(), "wrongPassword");
        });

        assertEquals((new InvalidPaswordOrEmailException()).getMessage(), exception.getMessage());
    }
}
