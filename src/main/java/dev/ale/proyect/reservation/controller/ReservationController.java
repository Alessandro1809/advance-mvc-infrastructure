package dev.ale.proyect.reservation.controller;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Reservation;
import dev.ale.proyect.reservation.service.ReservationService;
import dev.ale.proyect.reservation.service.ReservationValidators;

import java.util.List;
import java.util.Optional;

public class ReservationController {

    //first we need call the service of Reservations
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    //Here, we need to create the methods that call the service methods

    public void addReservation(Reservation reservation) throws InvalidReservationException {
        ReservationValidators.validate(reservation);
        reservationService.saveReservation(reservation);
    }

    public void updateReservation(Reservation reservation) throws InvalidReservationException {
        ReservationValidators.validate(reservation);
        reservationService.updateReservation(reservation);
    }

    public void removeReservation(Long id) throws InvalidReservationException {
        ReservationValidators.validateId(id);
        reservationService.deleteReservation(id);
    }

    public List<Reservation> getAllReservations() throws InvalidReservationException {
        return reservationService.getAllReservations();
    }

    public Optional<Reservation> getReservationById(Long id) throws InvalidReservationException {
        ReservationValidators.validateId(id);
        return reservationService.getReservationById(id);
    }
}
