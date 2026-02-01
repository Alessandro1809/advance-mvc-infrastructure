package dev.ale.proyect.reservation.repository;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.interfaces.IEventRepository;
import dev.ale.proyect.reservation.model.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventsRepository implements IEventRepository {
    //In-memory list to store events
     private final List<Events> events = new ArrayList<>();

    @Override
    public List<Events> allEvents() throws InvalidReservationException {
        if (events.isEmpty()){
            throw new InvalidReservationException("La lista de eventos está vacía.");
        }
        return new ArrayList<>(events);
    }

    @Override
    public Optional<Events> findEventByTitle(String title) {
        return events.stream().filter(event -> event.getTitle().equals(title)).findFirst();
    }

    @Override
    public Optional<Events> getByIdEvent(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id)).findFirst();
    }

    @Override
    public void updateEvent(Events event) throws InvalidReservationException {
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
    public void saveEvent(Events event) {
        events.add(event);

    }

    @Override
    public void deleteEvent(Long id) {
        events.remove(id);

    }

    @Override
    public boolean existsEventById(Long id) {
        return events.stream().anyMatch(event -> event.getId().equals(id));
    }

    //find index of event by ID
    public int findEventIndex(Long id) {
        for(int i =0; i < events.size(); i++){
            if (events.get(i).getId().equals(id)){
                return i;
            }
        }

        return -1;
    }
}
