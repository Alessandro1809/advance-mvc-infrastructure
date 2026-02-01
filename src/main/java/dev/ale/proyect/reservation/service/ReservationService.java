package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Events;
import dev.ale.proyect.reservation.model.ReservationStatus;
import dev.ale.proyect.reservation.model.Reservations;
import dev.ale.proyect.reservation.repository.EventsRepository;
import dev.ale.proyect.reservation.repository.ReservationRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReservationService {

    //Instance of reservation repository
    private final ReservationRepository reservationRepository;
    private final EventsRepository eventsRepository;

    public ReservationService(ReservationRepository reservationRepository, EventsRepository eventsRepository) {
        this.reservationRepository = reservationRepository;
        this.eventsRepository = eventsRepository;
    }

    //get all reservations
    public List<Reservations> getAllReservations() throws InvalidReservationException {
        return reservationRepository.allReservations();
    }

    //get Reservation by id
    public Optional<Reservations> getReservationById(Long id) throws InvalidReservationException {
        return reservationRepository.findByIdReservation(id);
    }

    //save Reservation
    public void saveReservation(Reservations reservation) throws InvalidReservationException {
        ReservationValidators.validate(reservation);

        Events event = eventsRepository.getByIdEvent(reservation.getEventId())
                .orElseThrow(() -> new InvalidReservationException(
                        "El evento con ID: " + reservation.getEventId() + " no existe. No se puede crear la reserva."
                ));

        int reservedSeats = reservationRepository.countReservedSeatsByEventId(event.getId());
        int requestedSeats = reservation.getSeats();


        if (reservedSeats + requestedSeats > event.getCapacity()) {
            throw new InvalidReservationException(
                    "No hay suficientes asientos disponibles para el evento con ID: " + event.getId() +
                            ". Capacidad del evento: " + event.getCapacity() +
                            ", Asientos reservados: " + reservedSeats +
                            ", Asientos solicitados: " + requestedSeats
            );
        }

        reservationRepository.saveReservation(reservation);

    }

    //update Reservation
    public void updateReservation(Reservations reservation) throws InvalidReservationException {
        //validates reservtion object
        ReservationValidators.validate(reservation);

        //the reservation may be updated only if exists
        Reservations existingReservation = reservationRepository.findByIdReservation(reservation.getId())
                .orElseThrow(()-> new InvalidReservationException("El evento con ID: "
                        + reservation.getId()
                        + " no existe. No se puede actualizar la reserva."));

        Events event = eventsRepository.getByIdEvent(reservation.getEventId())
                .orElseThrow(() -> new InvalidReservationException(
                        "El evento con ID: " + reservation.getEventId() + " no existe."
                ));

        //If the reservation status is CONFIRMER, check seat availability
        if(reservation.getStatus() == ReservationStatus.CONFIRMED ){
            int reservedSeats = reservationRepository.countReservedSeatsByEventId(reservation.getEventId());
            int requestedSeats = reservation.getSeats();

            //If the existing reservation is also CONFIRMED and for the same event, we need to subtract its seats from the total reserved seats
            if(existingReservation.getStatus() == ReservationStatus.CONFIRMED && Objects.equals(existingReservation.getEventId(), reservation.getEventId())){
                requestedSeats = reservation.getSeats() - existingReservation.getSeats();
            }

            //Check if there are enough seats available
            if (requestedSeats + reservation.getSeats() > event.getCapacity()){
                throw new InvalidReservationException(
                        "No hay suficientes asientos disponibles para el evento con ID: " + event.getId() +
                                ". Capacidad del evento: " + event.getCapacity() +
                                ", Asientos reservados: " + reservedSeats +
                                ", Asientos solicitados: " + requestedSeats
                );
            }

        }

        reservationRepository.updateReservation(reservation);
    }

}
