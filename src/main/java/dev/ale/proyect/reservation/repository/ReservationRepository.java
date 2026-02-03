package dev.ale.proyect.reservation.repository;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.interfaces.IReservationRepository;
import dev.ale.proyect.reservation.model.ReservationStatus;
import dev.ale.proyect.reservation.model.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReservationRepository implements IReservationRepository {


    //In-memory list to store reservations
    private final List<Reservation> reservations = new ArrayList<>();

    //Return all reservations from the list
    @Override
    public List<Reservation> allReservations() throws InvalidReservationException {
        //return a new list with all elemnts from reservations
        return new ArrayList<>(reservations);
    }

    @Override
    public Optional<Reservation> findByIdReservation(Long id) {
        //Find reservation by ID using stream filter(remember that we use Optional for possible null values)
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst();
    }


    @Override
    public void updateReservation(Reservation reservation) throws InvalidReservationException {
        //Direct null check for reservation and its ID
        //Remember that Optional is not used here because Optional is not for validate individual parameters or only to avoid null checks
        if(reservation == null || reservation.getId() ==null){
            throw new InvalidReservationException("La reserva o el ID de la reserva no pueden ser nulos.");
        }
        int index = findReservationByIndex(reservation.getId());
        if(index != -1){
            reservations.set(index, reservation);
        } else {
            throw new InvalidReservationException("La reserva que intenta actualizar no existe.");
        }
    }


    //Check if a reservation exists by ID
    public int findReservationByIndex(Long id) {
        //We check if any reservation matches the given ID
        for(int i =0; i < reservations.size(); i++){
            if(Objects.equals(reservations.get(i).getId(), id)){
                return i;
            }
        }
        return -1;

    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public boolean existsReservationById(Long id) {
        return reservations.stream().anyMatch(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public int countReservedSeatsByEventId(Long eventId) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getEventId(), eventId))
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED
                        || reservation.getStatus() == ReservationStatus.PENDING)
                .mapToInt(Reservation::getSeats)
                .sum();
    }
}
