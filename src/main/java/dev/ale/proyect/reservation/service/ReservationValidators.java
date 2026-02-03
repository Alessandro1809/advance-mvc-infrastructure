package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Reservation;

import java.time.LocalDate;

public class ReservationValidators {
    public static void validate(Reservation reservation) throws InvalidReservationException {
        if (reservation == null) {
            throw new InvalidReservationException("La reserva no puede ser nula");
        }

        if (reservation.getSeats() <=0){
            throw new InvalidReservationException("La cantidad de asientos debe ser mayor a 0");
        }

        if (reservation.getDate() == null) {
            throw new InvalidReservationException("La fecha de la reserva no puede ser nula");
        }

        if (reservation.getDate().isBefore(LocalDate.now())){
            throw new InvalidReservationException("La fecha de la reserva no puede ser una fecha pasad, por favor ingrese una fecha valida");
        }

        if (reservation.getCustomerEmail() == null || reservation.getCustomerEmail().isBlank()) {
            throw new InvalidReservationException("El correo electronico no puede ser nulo o vacio");
        }

        if (!reservation.getCustomerEmail().contains("@")){
            throw new InvalidReservationException("El correo electronico no es valido");
        }

        if (reservation.getStatus() == null){
            throw new InvalidReservationException("El estado de la reserva no puede ser nulo");
        }
    }
    public static void validateId(Long id) throws InvalidReservationException {
        if (id == null || id <= 0) {
            throw new InvalidReservationException("El ID de la reserva no puede ser nulo o menor o igual a 0");
        }
    }

}
