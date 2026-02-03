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

        int requestedSeats = reservation.getSeats();
        if (reservesSeats(reservation.getStatus())) {
            if (requestedSeats > event.getCapacity()) {
                throw new InvalidReservationException(
                        "No hay suficientes asientos disponibles para el evento con ID: " + event.getId() +
                                ". Asientos disponibles: " + event.getCapacity() +
                                ", Asientos solicitados: " + requestedSeats
                );
            }
            event.setCapacity(event.getCapacity() - requestedSeats);
            eventsRepository.updateEvent(event);
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

        Event newEvent = eventsRepository.getByIdEvent(reservation.getEventId())
                .orElseThrow(() -> new InvalidReservationException(
                        "El evento con ID: " + reservation.getEventId() + " no existe."
                ));

        int availableSeatsForNewEvent = newEvent.getCapacity();
        if (reservesSeats(reservation.getStatus())) {
            if (reservesSeats(existingReservation.getStatus())
                    && Objects.equals(existingReservation.getEventId(), reservation.getEventId())) {
                availableSeatsForNewEvent += existingReservation.getSeats();
            }
            if (reservation.getSeats() > availableSeatsForNewEvent) {
                throw new InvalidReservationException(
                        "No hay suficientes asientos disponibles para el evento con ID: " + newEvent.getId() +
                                ". Asientos disponibles: " + availableSeatsForNewEvent +
                                ", Asientos solicitados: " + reservation.getSeats()
                );
            }
        }

        if (reservesSeats(existingReservation.getStatus())) {
            Event oldEvent = eventsRepository.getByIdEvent(existingReservation.getEventId())
                    .orElseThrow(() -> new InvalidReservationException(
                            "El evento con ID: " + existingReservation.getEventId() + " no existe."
                    ));
            oldEvent.setCapacity(oldEvent.getCapacity() + existingReservation.getSeats());
            eventsRepository.updateEvent(oldEvent);
        }

        if (reservesSeats(reservation.getStatus())) {
            newEvent.setCapacity(newEvent.getCapacity() - reservation.getSeats());
            eventsRepository.updateEvent(newEvent);
        }

        reservationRepository.updateReservation(reservation);
    }

    public void deleteReservation(Long id) throws InvalidReservationException {
        Optional<Reservation> existingReservation = reservationRepository.findByIdReservation(id);
        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();
            if (reservesSeats(reservation.getStatus())) {
                Event event = eventsRepository.getByIdEvent(reservation.getEventId())
                        .orElseThrow(() -> new InvalidReservationException(
                                "El evento con ID: " + reservation.getEventId() + " no existe."
                        ));
                event.setCapacity(event.getCapacity() + reservation.getSeats());
                eventsRepository.updateEvent(event);
            }
            reservationRepository.deleteReservation(id);
        } else {
            throw new InvalidReservationException("La reserva con ID: " + id + " no existe.");
        }
    }

    private boolean reservesSeats(ReservationStatus status) {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.PENDING;
    }

}
