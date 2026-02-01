package dev.ale.proyect.reservation.interfaces;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Reservations;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository {
    List<Reservations> allReservations() throws InvalidReservationException;
    Optional<Reservations> findByIdReservation(Long id);
    void updateReservation(Reservations reservation) throws InvalidReservationException;
    void saveReservation(Reservations reservation);
    void deleteReservation(Long id);
    boolean existsReservationById(Long id);
    int countReservedSeatsByEventId(Long eventId);
}
