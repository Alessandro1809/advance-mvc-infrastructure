package dev.ale.proyect.reservation.service;

import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Event;

import java.time.LocalDate;

public class EventValidator {

    public static void validate(Event event) throws InvalidReservationException {
        if (event == null) {
            throw new InvalidReservationException("El evento no puede ser nulo");
        }

        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new InvalidReservationException("El titulo del evento no puede ser nulo o vacio");
        }

        if (event.getCapacity() <= 0) {
            throw new InvalidReservationException("La capacidad del evento debe ser mayor a 0");
        }

        if (event.getDate() == null) {
            throw new InvalidReservationException("La fecha del evento no puede ser nula y debe ser valida");
        }

        if (event.getDate().isBefore(LocalDate.now())){
            throw new InvalidReservationException("La fecha del evento no puede ser una fecha pasada, por favor ingrese una fecha valida");
        }
    }


}
