package com.alejjandrodev.tableReservation.services;

import com.alejjandrodev.tableReservation.errors.exceptions.NotAllowedToModifyReservation;
import com.alejjandrodev.tableReservation.errors.exceptions.TableReservationNotFoundException;
import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.TableReservation;
import com.alejjandrodev.tableReservation.repositories.TableReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TableReservationServiceTest {

    @InjectMocks
    private TableReservationService tableReservationService;

    @Mock
    private TableReservationRepository tableReservationRepository;

    private ServerUser serverUser;
    private TableReservation tableReservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        serverUser = createMockServerUser(1L);

        tableReservation = createMockReservation();
    }

    private ServerUser createMockServerUser(Long id) {
        ServerUser mockServerUser = new ServerUser();
        mockServerUser.setId(id);
        mockServerUser.setName("Alejandro Marcano");
        mockServerUser.setEmail("alejjandromar@test.com");
        return mockServerUser;
    }

    private TableReservation createMockReservation() {
        TableReservation mockReservation = new TableReservation();
        mockReservation.setId(1L);
        mockReservation.setGuestName("Alice");
        mockReservation.setGuestNumber(4);
        mockReservation.setNotes("Birthday party");
        mockReservation.setServerBy(serverUser);
        return mockReservation;
    }

    @Test
    public void testCreateReservation() {
        when(tableReservationRepository.save(any(TableReservation.class))).thenReturn(tableReservation);

        TableReservation created = tableReservationService.createReservation(tableReservation, serverUser);

        assertNotNull(created);
        assertEquals(tableReservation.getGuestName(), created.getGuestName());
        verify(tableReservationRepository, times(1)).save(tableReservation);
    }

    @Test
    public void testGetAllReservations() {
        List<TableReservation> reservations = new ArrayList<>();
        reservations.add(tableReservation);

        when(tableReservationRepository.findAll()).thenReturn(reservations);

        List<TableReservation> result = tableReservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals(tableReservation.getGuestName(), result.get(0).getGuestName());
    }

    @Test
    public void testGetReservationById_Success() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        TableReservation result = tableReservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(tableReservation.getId(), result.getId());
    }

    @Test
    public void testGetReservationById_NotFound() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TableReservationNotFoundException.class, () -> {
            tableReservationService.getReservationById(1L);
        });

        assertEquals((new TableReservationNotFoundException(1L)).getMessage(), exception.getMessage());
    }

    @Test
    public void testUpdateReservation_Success() {
        TableReservation updatedMockedResponse = createMockReservation();
        updatedMockedResponse.setGuestName("Bob");

        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));
        when(tableReservationRepository.save(any(TableReservation.class))).thenReturn(updatedMockedResponse);

        TableReservation updatedTable = new TableReservation();
        updatedTable.setGuestName("Bob");

        TableReservation result = tableReservationService.updateReservation(1L, updatedTable, serverUser);

        assertEquals(updatedTable.getGuestName(), result.getGuestName());
        verify(tableReservationRepository, times(1)).save(any(TableReservation.class));
    }

    @Test
    public void testUpdateReservation_NotAllowed() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        ServerUser otherUser = createMockServerUser(2L);

        Exception exception = assertThrows(NotAllowedToModifyReservation.class, () -> {
            tableReservationService.updateReservation(1L, new TableReservation(), otherUser);
        });

        assertEquals((new NotAllowedToModifyReservation(1L)).getMessage(), exception.getMessage());
    }

    @Test
    public void testDelete_Success() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        tableReservationService.deleteReservation(1L, serverUser);

        verify(tableReservationRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDelete_NotAllowed() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        ServerUser otherUser = createMockServerUser(2L);

        Exception exception = assertThrows(NotAllowedToModifyReservation.class, () -> {
            tableReservationService.deleteReservation(1L, otherUser);
        });

        assertEquals((new NotAllowedToModifyReservation(1L)).getMessage(), exception.getMessage());
    }

    @Test
    public void testGiveUp_Success() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        tableReservationService.giveUp(1L, serverUser);

        assertNull(tableReservation.getServerBy());
        verify(tableReservationRepository, times(1)).save(any(TableReservation.class));
    }

    @Test
    public void testGiveUp_NotAllowed() {
        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(tableReservation));

        ServerUser otherUser = createMockServerUser(2L);

        Exception exception = assertThrows(NotAllowedToModifyReservation.class, () -> {
            tableReservationService.giveUp(1L, otherUser);
        });

        assertEquals((new NotAllowedToModifyReservation(1L)).getMessage(), exception.getMessage());
    }

    @Test
    public void testTakeIt_Success() {
        TableReservation mockResult =  createMockReservation();
        mockResult.setServerBy(null);

        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(mockResult));

        tableReservationService.takeIt(1L, serverUser);

        verify(tableReservationRepository, times(1)).save(any(TableReservation.class));
    }

    @Test
    public void testTakeIt_AlreadyTaken() {


        when(tableReservationRepository.findById(1L)).thenReturn(Optional.of(createMockReservation()));

        Exception exception = assertThrows(NotAllowedToModifyReservation.class, () -> {
            tableReservationService.takeIt(1L, serverUser);
        });

        assertEquals((new NotAllowedToModifyReservation(1L)).getMessage(), exception.getMessage());
    }
}
