package dev.ale.proyect.reservation.service;


import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Events;
import dev.ale.proyect.reservation.repository.EventsRepository;

import java.util.List;
import java.util.Optional;

//Make a service class is a good practice to separate the business logic from the controller and repository layers.

//Make asier the dependencies injection principle

//Improve the testability of the code

public class EventService {

    private final EventsRepository eventsRepository;

    //constructor of the class
    public EventService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    //Get all events
    public List<Events> getAllEvents() throws InvalidReservationException {
        return eventsRepository.allEvents();
    }
    //Get event by id
    public Optional<Events> getEventById(Long id) {
        return eventsRepository.getByIdEvent(id);
    }

    public Optional<Events> getEventsByTitle(String title) throws InvalidReservationException {
        String ti = title.trim();
        return eventsRepository.findEventByTitle(ti);
    }

    //save Event
    public void saveEvent( Events event) throws InvalidReservationException{
    //Here is where we write the logic to save an event
    EventValidator.validate(event);//This class only validate the event object
    if (!eventsRepository.existsEventById(event.getId())){//check if the event already exists
        eventsRepository.saveEvent(event);
        System.out.println("\nEvento Guardado"+"\nID del evento:"+ event.getId() +"\nEl evento:" + event.getTitle() + " ha sido guardado exitosamente.");
    }else {
        throw new InvalidReservationException("El evento con ID: " + event.getId() + " ya existe.");
    }
    }

    //delete Event
    public void deleteEvent(Long id) throws InvalidReservationException {
        Optional<Events> eventOptional = eventsRepository.getByIdEvent(id);//check if the event exists with an optional object
        if (eventOptional.isPresent()){
            eventsRepository.deleteEvent(id);
            System.out.println("\nEvento Eliminado"+"\nID del evento:"+ id +"\nEl evento ha sido eliminado exitosamente.");
        } else {
            throw new InvalidReservationException("El evento con ID: " + id + " no existe.");
        }
    }

    //update Event
    public void updateEvent(Events event) throws InvalidReservationException {
        EventValidator.validate(event);//We need to validate the new event data
        Optional<Events> existingEvent = eventsRepository.getByIdEvent(event.getId());//check if the event exists with an optional object
        if(existingEvent.isPresent()){
            eventsRepository.updateEvent(event);
            System.out.println("\nEvento Actualizado"+"\nID del evento:"+ event.getId() +"\nEl evento:" + event.getTitle() + " ha sido actualizado exitosamente.");
        } else {
            throw new InvalidReservationException("El evento con ID: " + event.getId() + " no existe.");
        }
    }


}
