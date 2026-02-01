package dev.ale.proyect.reservation.interfaces;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Event;

import java.util.List;
import java.util.Optional;

public interface IEventRepository {
    List<Event> allEvents() throws InvalidReservationException;
    Optional<Event> getByIdEvent(Long id);
    Optional<Event> findEventByTitle(String title);
    void updateEvent(Event event) throws InvalidReservationException;
    void saveEvent(Event event);
    void deleteEvent(Long id);
    boolean existsEventById(Long id);
}
