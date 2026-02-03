package dev.ale.proyect.reservation.controller;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Event;
import dev.ale.proyect.reservation.service.EventService;
import dev.ale.proyect.reservation.service.EventValidator;

import java.util.List;
import java.util.Optional;

public class EventController {

    //Vamos a hacer uso del servicio del EventService
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    //Methods that call the service methods
    public void addEvent(Event event) throws InvalidReservationException {
        //We need to validate the event before saving it
        EventValidator.validate(event);
        //Now we can save the event using the service(remember the controller only calls the service methods)
        eventService.saveEvent(event);
    }
    public void AddEvent(Event event) throws InvalidReservationException {
        addEvent(event);
    }
    public void RemoveEvent(Long id) throws InvalidReservationException {
        EventValidator.validateId(id);
        eventService.deleteEvent(id);
    }
    public void removeEvent(Long id) throws InvalidReservationException {
        RemoveEvent(id);
    }
    public void updateEvent(Event event) throws InvalidReservationException {
        EventValidator.validate(event);
        eventService.updateEvent(event);
    }
    public List<Event> getAllEvents() throws InvalidReservationException {
        return eventService.getAllEvents();
    }

    public Optional<Event> getEventById(Long id) throws InvalidReservationException {
        EventValidator.validateId(id);
        return eventService.getEventById(id);
    }

    public Optional<Event> getEventByTitle(String title) throws InvalidReservationException {
        if (title == null || title.isBlank()) {
            throw new InvalidReservationException("El titulo no puede ser nulo o vacio");
        }
        String ti = title.trim();
        return eventService.getEventsByTitle(ti);
    }


}
