package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Reservations;

import java.time.LocalDate;

public class ReservationValidators {
    public static void validate(Reservations reservation) throws InvalidReservationException {

        if (reservation.getSeats() <=0){
            throw new InvalidReservationException("La cantidad de asientos debe ser mayor a 0");
        }

        if (reservation.getDate().isBefore(LocalDate.now())){
            throw new InvalidReservationException("La fecha de la reserva no puede ser una fecha pasad, por favor ingrese una fecha valida");
        }

        if (!reservation.getCustomerEmail().contains("@")){
            throw new InvalidReservationException("El correo electronico no es valido");
        }

        if (reservation.getStatus() == null){
            throw new InvalidReservationException("El estado de la reserva no puede ser nulo");
        }
    }

}
