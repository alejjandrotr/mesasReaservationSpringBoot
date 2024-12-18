package com.alejjandrodev.tableReservation.e2e;

import com.alejjandrodev.tableReservation.dtos.LoginDto;
import com.alejjandrodev.tableReservation.dtos.RegisterDto;
import com.alejjandrodev.tableReservation.dtos.TableReservationDto;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.TableReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TableReservationE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String user1Token;
    private String user2Token;

    @BeforeEach
    public void setUp() {
        // Create first user and login to get token
        RegisterDto user1 = new RegisterDto();
        user1.setName("User One");
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");
        user1.setConfirmPassword("password123");

        restTemplate.postForEntity("/auth/register", user1, Void.class);
        LoginDto loginDto1 = new LoginDto();
        loginDto1.setEmail("user1@example.com");
        loginDto1.setPassword("password123");
        ResponseEntity<String> response1 = restTemplate.postForEntity("/auth/login", loginDto1, String.class);
        user1Token = response1.getBody();

        // Create second user and login to get token
        RegisterDto user2 = new RegisterDto();
        user2.setName("User Two");
        user2.setEmail("user2@example.com");
        user2.setPassword("password123");
        user2.setConfirmPassword("password123");

        restTemplate.postForEntity("/auth/register", user2, Void.class);
        LoginDto loginDto2 = new LoginDto();
        loginDto2.setEmail("user2@example.com");
        loginDto2.setPassword("password123");
        ResponseEntity<String> response2 = restTemplate.postForEntity("/auth/login", loginDto2, String.class);
        user2Token = response2.getBody();
    }

    @Test
    public void testUserCanCreateAndEditReservations() {
        // User 1 creates a reservation
        TableReservationDto reservationDto = new TableReservationDto();
        reservationDto.setGuestName("Alice");
        reservationDto.setGuestNumber(4);
        reservationDto.setNotes("Birthday party");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user1Token);

        ResponseEntity<TableReservation> createResponse = restTemplate.postForEntity(
                "/reservations",
                new HttpEntity<>(reservationDto, headers),
                TableReservation.class);

        assertThat(createResponse.getStatusCodeValue()).isEqualTo(200);

        // User 1 edits the reservation
        TableReservation createdReservation = createResponse.getBody();

        assert createdReservation != null; // Ensure it's not null

        createdReservation.setNotes("Updated birthday party notes");

        ResponseEntity<TableReservation> updateResponse = restTemplate.exchange(
                "/reservations/" + createdReservation.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(createdReservation, headers),
                TableReservation.class);

        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(200);

        // Verify the updated notes
        TableReservation updatedReservation = updateResponse.getBody();

        assert updatedReservation != null; // Ensure it's not null
        assertThat(updatedReservation.getNotes()).isEqualTo("Updated birthday party notes");
    }

    @Test
    public void testUserCannotEditAnotherUsersReservations() {
        // User 2 tries to edit User 1's reservation
        TableReservationDto reservationDto = new TableReservationDto();
        reservationDto.setGuestName("Bob");
        reservationDto.setGuestNumber(3);

        HttpHeaders headersUser1 = new HttpHeaders();
        headersUser1.setBearerAuth(user1Token);

        ResponseEntity<TableReservation> createResponse = restTemplate.postForEntity(
                "/reservations",
                new HttpEntity<>(reservationDto, headersUser1),
                TableReservation.class);

        TableReservation createdReservation = createResponse.getBody();

        assert createdReservation != null; // Ensure it's not null

        HttpHeaders headersUser2 = new HttpHeaders();
        headersUser2.setBearerAuth(user2Token);

        createdReservation.setNotes("Trying to change from another user");

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                "/reservations/" + createdReservation.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(createdReservation, headersUser2),
                Void.class);

        assertThat(updateResponse.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testGiveUpAndTakeIt() {
        // User 1 creates a reservation
        TableReservationDto reservationDto = new TableReservationDto();
        reservationDto.setGuestName("Charlie");
        reservationDto.setGuestNumber(5);
        reservationDto.setNotes("Family gathering");

        HttpHeaders headersUser1 = new HttpHeaders();
        headersUser1.setBearerAuth(user1Token);

        ResponseEntity<TableReservation> createResponse = restTemplate.postForEntity(
                "/reservations",
                new HttpEntity<>(reservationDto, headersUser1),
                TableReservation.class);

        TableReservation createdReservation = createResponse.getBody();

        assertThat(createdReservation).isNotNull(); // Ensure it's not null

        // User 1 gives up the reservation
        ResponseEntity<Void> giveUpResponse = restTemplate.postForEntity(
                "/reservations/" + createdReservation.getId() + "/give-up",
                new HttpEntity<>(null, headersUser1),
                Void.class);

        assertThat(giveUpResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // User 2 takes the reservation
        HttpHeaders headersUser2 = new HttpHeaders();
        headersUser2.setBearerAuth(user2Token);

        ResponseEntity<Void> takeItResponse = restTemplate.postForEntity(
                "/reservations/" + createdReservation.getId() + "/take-it",
                new HttpEntity<>(null, headersUser2),
                Void.class);

        assertThat(takeItResponse.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
