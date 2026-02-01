package dev.ale.proyect.reservation.interfaces;

import dev.ale.proyect.reservation.model.Events;
import dev.ale.proyect.reservation.model.Reservations;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Events> allEvents();

    List<Reservations> allReservations();
    Optional<Events> findEventByTitle(String title);
    
    Optional<Events> finfByIdEvent(Long id);
    Optional<Reservations> findByIdReservation(Long id);
    void saveReservation(Reservations reservation);
    void deleteReservation(Long id);

    void updateReservation(Reservations reservation);

    void updateEvent(Events event);

    void saveEvent(Events event);

    void deleteEvent(Long id);

    boolean existsEventById(Long id);

    boolean existsReservationById(Long id);

}
