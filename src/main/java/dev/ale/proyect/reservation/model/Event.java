package dev.ale.proyect.reservation.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long id;
    private String title;
    private int capacity;
    private LocalDate date;
    private EventTags tag;
}
