package dev.ale.proyect.reservation;

import dev.ale.proyect.reservation.controller.EventController;
import dev.ale.proyect.reservation.controller.ReservationController;
import dev.ale.proyect.reservation.interfaces.IEventRepository;
import dev.ale.proyect.reservation.interfaces.IReservationRepository;
import dev.ale.proyect.reservation.repository.EventsRepository;
import dev.ale.proyect.reservation.repository.ReservationRepository;
import dev.ale.proyect.reservation.service.EventService;
import dev.ale.proyect.reservation.service.ReservationService;
import dev.ale.proyect.reservation.view.View;

public class Main {
    static void main() {
        IEventRepository eventsRepository = new EventsRepository();
        IReservationRepository reservationRepository = new ReservationRepository();

        EventService eventService = new EventService(eventsRepository);
        ReservationService reservationService = new ReservationService(reservationRepository, eventsRepository);

        EventController eventController = new EventController(eventService);
        ReservationController reservationController = new ReservationController(reservationService);

        View view = new View(eventController, reservationController);
        view.start();
    }
}
