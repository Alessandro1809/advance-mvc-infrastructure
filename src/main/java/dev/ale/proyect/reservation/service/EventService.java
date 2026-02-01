package dev.ale.proyect.reservation.service;


import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.interfaces.IEventRepository;
import dev.ale.proyect.reservation.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

//Make a service class is a good practice to separate the business logic from the controller and repository layers.

//Make asier the dependencies injection principle

//Improve the testability of the code

public class EventService {

    private static final Logger LOGGER = Logger.getLogger(EventService.class.getName());

    private final IEventRepository eventsRepository;

    //constructor of the class
    public EventService(IEventRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    //Get all events
    public List<Event> getAllEvents() throws InvalidReservationException {
        return eventsRepository.allEvents();
    }
    //Get event by id
    public Optional<Event> getEventById(Long id) {
        return eventsRepository.getByIdEvent(id);
    }

    public Optional<Event> getEventsByTitle(String title) throws InvalidReservationException {
        if (title == null || title.isBlank()) {
            throw new InvalidReservationException("El titulo no puede ser nulo o vacio");
        }
        String ti = title.trim();
        return eventsRepository.findEventByTitle(ti);
    }

    //save Event
    public void saveEvent( Event event) throws InvalidReservationException{
    //Here is where we write the logic to save an event
    EventValidator.validate(event);//This class only validate the event object
    if (!eventsRepository.existsEventById(event.getId())){//check if the event already exists
        eventsRepository.saveEvent(event);
        LOGGER.info("Evento guardado. ID: " + event.getId() + ", titulo: " + event.getTitle());
    }else {
        throw new InvalidReservationException("El evento con ID: " + event.getId() + " ya existe.");
    }
    }

    //delete Event
    public void deleteEvent(Long id) throws InvalidReservationException {
        Optional<Event> eventOptional = eventsRepository.getByIdEvent(id);//check if the event exists with an optional object
        if (eventOptional.isPresent()){
            eventsRepository.deleteEvent(id);
            LOGGER.info("Evento eliminado. ID: " + id);
        } else {
            throw new InvalidReservationException("El evento con ID: " + id + " no existe.");
        }
    }

    //update Event
    public void updateEvent(Event event) throws InvalidReservationException {
        EventValidator.validate(event);//We need to validate the new event data
        Optional<Event> existingEvent = eventsRepository.getByIdEvent(event.getId());//check if the event exists with an optional object
        if(existingEvent.isPresent()){
            eventsRepository.updateEvent(event);
            LOGGER.info("Evento actualizado. ID: " + event.getId() + ", titulo: " + event.getTitle());
        } else {
            throw new InvalidReservationException("El evento con ID: " + event.getId() + " no existe.");
        }
    }


}
