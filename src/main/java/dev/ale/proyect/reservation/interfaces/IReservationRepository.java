package dev.ale.proyect.reservation.interfaces;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository {
    List<Reservation> allReservations() throws InvalidReservationException;
    Optional<Reservation> findByIdReservation(Long id);
    void updateReservation(Reservation reservation) throws InvalidReservationException;
    void saveReservation(Reservation reservation);
    void deleteReservation(Long id);
    boolean existsReservationById(Long id);
    int countReservedSeatsByEventId(Long eventId);
}
