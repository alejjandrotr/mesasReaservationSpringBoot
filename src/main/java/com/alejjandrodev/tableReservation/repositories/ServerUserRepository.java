package com.alejjandrodev.tableReservation.repositories;

import com.alejjandrodev.tableReservation.models.ServerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ServerUserRepository extends JpaRepository<ServerUser, Long> {
    Optional<ServerUser> findByEmail(String email);
}