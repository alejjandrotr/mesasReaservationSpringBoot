package com.alejjandrodev.tableReservation.services;

import com.alejjandrodev.tableReservation.errors.exceptions.NotAllowedToModifyReservation;
import com.alejjandrodev.tableReservation.errors.exceptions.TableReservationNotFoundException;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.TableReservation;
import com.alejjandrodev.tableReservation.repositories.TableReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableReservationService {

    @Autowired
    private TableReservationRepository tableReservationRepository;

    public TableReservation createReservation(TableReservation tableReservation, ServerUser serverUser) {
        tableReservation.setServerBy(serverUser); // Set the logged-in user as the server
        return tableReservationRepository.save(tableReservation);
    }

    public List<TableReservation> getAllReservations() {
        return tableReservationRepository.findAll();
    }

    public TableReservation getReservationById(Long id) {
        return tableReservationRepository.findById(id)
                .orElseThrow(() -> new TableReservationNotFoundException(id));
    }

    public List<TableReservation> getReservationsByUser(ServerUser serverUser) {
        return tableReservationRepository.findByServerBy(serverUser);
    }

    public List<TableReservation> getAllReservationsWithoutServer() {
        List<TableReservation> reservations = tableReservationRepository.findAll();
        reservations.forEach(reservation -> reservation.setServerBy(null));
        return reservations;
    }

    public TableReservation updateReservation(Long id, TableReservation updatedTable, ServerUser serverUser) {
        TableReservation existingTable = getReservationById(id);
        if (!isTheSameUser(serverUser, existingTable)) {
            throw new NotAllowedToModifyReservation(id);
        }
        updatedTable.setId(id); // Ensure the ID is set for the update
        return tableReservationRepository.save(updatedTable);
    }

    public void deleteReservation(Long id, ServerUser serverUser) {
        TableReservation existingTable = getReservationById(id);
        if (!isTheSameUser(serverUser, existingTable)) {
            throw new NotAllowedToModifyReservation(id);
        }
        tableReservationRepository.deleteById(id);
    }

    public void giveUp(Long id, ServerUser serverUser) {
        TableReservation existingTable = getReservationById(id);
        if (!isTheSameUser(serverUser, existingTable)) {
            throw new NotAllowedToModifyReservation(id);
        }
        existingTable.setServerBy(null);
        tableReservationRepository.save(existingTable);
    }

    private static boolean isTheSameUser(ServerUser serverUser, TableReservation existingTable) {
        return existingTable.getServerBy().getId().longValue() == serverUser.getId().longValue();
    }

    public void takeIt(Long id, ServerUser serverUser) {
        TableReservation existingTable = getReservationById(id);
        if (existingTable.getServerBy() != null) {
            throw new NotAllowedToModifyReservation(id);
        }
        existingTable.setServerBy(serverUser);
        tableReservationRepository.save(existingTable);
    }
}
