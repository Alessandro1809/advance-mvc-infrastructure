package dev.ale.proyect.reservation.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events {
    Long id;
    String title;
    int capacity;
    LocalDate date;
    EventTags tag;
}
