package dev.ale.proyect.reservation.repository;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.interfaces.IEventRepository;
import dev.ale.proyect.reservation.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EventsRepository implements IEventRepository {
    //In-memory list to store events
     private final List<Event> events = new ArrayList<>();

    @Override
    public List<Event> allEvents() throws InvalidReservationException {
        return new ArrayList<>(events);
    }

    @Override
    public Optional<Event> findEventByTitle(String title) {
        return events.stream().filter(event -> Objects.equals(event.getTitle(), title)).findFirst();
    }

    @Override
    public Optional<Event> getByIdEvent(Long id) {
        return events.stream()
                .filter(event -> Objects.equals(event.getId(), id)).findFirst();
    }

    @Override
    public void updateEvent(Event event) throws InvalidReservationException {
        if(event == null || event.getId() == null){
            throw new InvalidReservationException("El evento o el ID del evento no pueden ser nulos.");
        }
        int index = findEventIndex(event.getId());
        if(index != -1){
            events.set(index, event);

        }else{
            throw new InvalidReservationException("El evento que intenta actualizar no existe");
        }
    }

    @Override
    public void saveEvent(Event event) {
        events.add(event);

    }

    @Override
    public void deleteEvent(Long id) {
        events.removeIf(event -> Objects.equals(event.getId(), id));

    }

    @Override
    public boolean existsEventById(Long id) {
        return events.stream().anyMatch(event -> Objects.equals(event.getId(), id));
    }

    //find index of event by ID
    public int findEventIndex(Long id) {
        for(int i =0; i < events.size(); i++){
            if (Objects.equals(events.get(i).getId(), id)){
                return i;
            }
        }

        return -1;
    }
}
