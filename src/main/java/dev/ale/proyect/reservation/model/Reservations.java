package dev.ale.proyect.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservations {
    Long id;
    Long eventId;
    String customerEmail;
    int seats;
    ReservationStatus status;
    LocalDate date;
}
