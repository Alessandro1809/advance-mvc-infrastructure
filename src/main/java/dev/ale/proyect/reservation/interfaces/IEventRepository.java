package dev.ale.proyect.reservation.interfaces;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Events;

import java.util.List;
import java.util.Optional;

public interface IEventRepository {
    List<Events> allEvents() throws InvalidReservationException;
    Optional<Events> getByIdEvent(Long id);
    Optional<Events> findEventByTitle(String title);
    void updateEvent(Events event) throws InvalidReservationException;
    void saveEvent(Events event);
    void deleteEvent(Long id);
    boolean existsEventById(Long id);
}
