package com.alejjandrodev.tableReservation.controllers;

import com.alejjandrodev.tableReservation.dtos.TableReservationDto;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.TableReservation;
import com.alejjandrodev.tableReservation.models.UserDetailModel;
import com.alejjandrodev.tableReservation.services.TableReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class TableReservationController {

    @Autowired
    private TableReservationService tableReservationService;

    @PostMapping
    public ResponseEntity<TableReservation> create(@Valid @RequestBody TableReservationDto tableReservationDto) {

        ServerUser serverUser = getServerUser();
        TableReservation tableReservation = new TableReservation();
        tableReservation.setGuestName(tableReservationDto.getGuestName());
        tableReservation.setGuestNumber(tableReservationDto.getGuestNumber());
        tableReservation.setNotes(tableReservationDto.getNotes());
        tableReservation.setServerBy(serverUser);

        TableReservation created = tableReservationService.createReservation(tableReservation, serverUser);
        return ResponseEntity.ok(created);
    }

    private static ServerUser getServerUser() {
        ServerUser serverUser;
        UserDetailModel user = (UserDetailModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        serverUser = user.getUser();
        return serverUser;
    }

    @GetMapping
    public ResponseEntity<List<TableReservation>> getAll() {
        List<TableReservation> reservations = tableReservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<TableReservation>> getMyReservations() {
        ServerUser serverUser = getServerUser();
        List<TableReservation> myReservations = tableReservationService.getReservationsByUser(serverUser);
        return ResponseEntity.ok(myReservations);
    }

    @GetMapping("/without-server")
    public ResponseEntity<List<TableReservation>> getAllWithoutServer() {
        List<TableReservation> reservations = tableReservationService.getAllReservationsWithoutServer();
        return ResponseEntity.ok(reservations);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TableReservation> getById(@PathVariable Long id) {
            TableReservation reservation = tableReservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
    }



    @PutMapping("/{id}")
    public ResponseEntity<TableReservation> update(@PathVariable Long id,
                                                   @Valid @RequestBody TableReservationDto updatedTableDto) {
        ServerUser serverUser = getServerUser();
        TableReservation updatedTable = new TableReservation();
        updatedTable.setId(id);
        updatedTable.setGuestName(updatedTableDto.getGuestName());
        updatedTable.setGuestNumber(updatedTableDto.getGuestNumber());
        updatedTable.setNotes(updatedTableDto.getNotes());

        TableReservation updated = tableReservationService.updateReservation(id, updatedTable, serverUser);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ServerUser serverUser = getServerUser();
        tableReservationService.deleteReservation(id, serverUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/give-up")
    public ResponseEntity<Void> giveUp(@PathVariable Long id) {
        ServerUser serverUser = getServerUser();
        tableReservationService.giveUp(id, serverUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/take-it") // Corrected method name
    public ResponseEntity<Void> takeIt(@PathVariable Long id) {
        ServerUser serverUser = getServerUser();
        tableReservationService.takeIt(id, serverUser); // Added missing comma
        return ResponseEntity.noContent().build();
    }



}
