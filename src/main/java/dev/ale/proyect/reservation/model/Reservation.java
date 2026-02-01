package dev.ale.proyect.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private Long eventId;
    private String customerEmail;
    private int seats;
    private ReservationStatus status;
    private LocalDate date;
}
