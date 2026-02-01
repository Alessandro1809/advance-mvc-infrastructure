package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Events;

public class EventValidator {

    public static void validate(Events event) throws InvalidReservationException {
        if (event.getCapacity() <= 0) {
            throw new InvalidReservationException("La capacidad del evento debe ser mayor a 0");
        }

        if (event.getDate() == null) {
            throw new InvalidReservationException("La fecha del evento no puede ser nula y debe ser valida");
        }

        if (event.getDate().isBefore(java.time.LocalDate.now())){
            throw new InvalidReservationException("La fecha del evento no puede ser una fecha pasada, por favor ingrese una fecha valida");
        }
    }


}
