package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.interfaces.IEventRepository;
import dev.ale.proyect.reservation.interfaces.IReservationRepository;
import dev.ale.proyect.reservation.model.Event;
import dev.ale.proyect.reservation.model.ReservationStatus;
import dev.ale.proyect.reservation.model.Reservation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReservationService {

    //Instance of reservation repository
    private final IReservationRepository reservationRepository;
    private final IEventRepository eventsRepository;

    public ReservationService(IReservationRepository reservationRepository, IEventRepository eventsRepository) {
        this.reservationRepository = reservationRepository;
        this.eventsRepository = eventsRepository;
    }

    //get all reservations
    public List<Reservation> getAllReservations() throws InvalidReservationException {
        return reservationRepository.allReservations();
    }

    //get Reservation by id
    public Optional<Reservation> getReservationById(Long id) throws InvalidReservationException {
        return reservationRepository.findByIdReservation(id);
    }

    //save Reservation
    public void saveReservation(Reservation reservation) throws InvalidReservationException {
        ReservationValidators.validate(reservation);

        Event event = eventsRepository.getByIdEvent(reservation.getEventId())
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
    public void updateReservation(Reservation reservation) throws InvalidReservationException {
        //validates reservtion object
        ReservationValidators.validate(reservation);

        //the reservation may be updated only if exists
        Reservation existingReservation = reservationRepository.findByIdReservation(reservation.getId())
                .orElseThrow(()-> new InvalidReservationException("La reserva con ID: "
                        + reservation.getId()
                        + " no existe. No se puede actualizar la reserva."));

        Event event = eventsRepository.getByIdEvent(reservation.getEventId())
                .orElseThrow(() -> new InvalidReservationException(
                        "El evento con ID: " + reservation.getEventId() + " no existe."
                ));

        //If the reservation status is CONFIRMED, check seat availability
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            int reservedSeats = reservationRepository.countReservedSeatsByEventId(reservation.getEventId());
            //If the existing reservation is also CONFIRMED and for the same event, subtract its seats from the total
            if (existingReservation.getStatus() == ReservationStatus.CONFIRMED
                    && Objects.equals(existingReservation.getEventId(), reservation.getEventId())) {
                reservedSeats -= existingReservation.getSeats();
            }

            //Check if there are enough seats available
            if (reservedSeats + reservation.getSeats() > event.getCapacity()) {
                throw new InvalidReservationException(
                        "No hay suficientes asientos disponibles para el evento con ID: " + event.getId() +
                                ". Capacidad del evento: " + event.getCapacity() +
                                ", Asientos reservados: " + reservedSeats +
                                ", Asientos solicitados: " + reservation.getSeats()
                );
            }
        }

        reservationRepository.updateReservation(reservation);
    }

}
