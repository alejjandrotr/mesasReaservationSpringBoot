package com.alejjandrodev.tableReservation.repositories;

import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.TableReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableReservationRepository extends JpaRepository<TableReservation, Long> {
    List<TableReservation> findByServerBy(ServerUser serverUser);
}
