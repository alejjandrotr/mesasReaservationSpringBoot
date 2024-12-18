package com.alejjandrodev.tableReservation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "tables")
public class TableReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Guest name cannot be empty")
    @Size(min = 2, message = "Guest name must be at least 2 characters long")
    private String guestName;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot be greater than 10")
    private int guestNumber;

    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime arriveAt;

    @ManyToOne
    @JoinColumn(name = "server_user_id", nullable = true)
    private ServerUser serverBy;

    // Constructor
    public TableReservation() {
        this.arriveAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getArriveAt() {
        return arriveAt;
    }

    public ServerUser getServerBy() {
        return serverBy;
    }

    public void setServerBy(ServerUser serverBy) {
        this.serverBy = serverBy;
    }
}
