package dev.ale.proyect.reservation.view;

import dev.ale.proyect.reservation.controller.EventController;
import dev.ale.proyect.reservation.controller.ReservationController;
import dev.ale.proyect.reservation.exception.InvalidReservationException;
import dev.ale.proyect.reservation.model.Event;
import dev.ale.proyect.reservation.model.EventTags;
import dev.ale.proyect.reservation.model.Reservation;
import dev.ale.proyect.reservation.model.ReservationStatus;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class View {
    private final EventController eventController;
    private final ReservationController reservationController;
    private final Scanner scanner;

    public View(EventController eventController, ReservationController reservationController) {
        this.eventController = eventController;
        this.reservationController = reservationController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int option = readInt("Opcion: ");
            switch (option) {
                case 1:
                    eventsMenu();
                    break;
                case 2:
                    reservationsMenu();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
        System.out.println("Hasta luego.");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Eventos");
        System.out.println("2. Reservas");
        System.out.println("0. Salir");
    }

    private void eventsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== Menu Eventos ===");
            System.out.println("1. Crear evento");
            System.out.println("2. Listar eventos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Buscar por titulo");
            System.out.println("5. Actualizar evento");
            System.out.println("6. Eliminar evento");
            System.out.println("0. Volver");
            int option = readInt("Opcion: ");
            switch (option) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    listEvents();
                    break;
                case 3:
                    findEventById();
                    break;
                case 4:
                    findEventByTitle();
                    break;
                case 5:
                    updateEvent();
                    break;
                case 6:
                    deleteEvent();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private void reservationsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== Menu Reservas ===");
            System.out.println("1. Crear reserva");
            System.out.println("2. Listar reservas");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar reserva");
            System.out.println("5. Eliminar reserva");
            System.out.println("0. Volver");
            int option = readInt("Opcion: ");
            switch (option) {
                case 1:
                    createReservation();
                    break;
                case 2:
                    listReservations();
                    break;
                case 3:
                    findReservationById();
                    break;
                case 4:
                    updateReservation();
                    break;
                case 5:
                    deleteReservation();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        }
    }

    private void createEvent() {
        try {
            Long id = readLong("ID del evento: ");
            String title = readRequired("Titulo: ");
            int capacity = readInt("Capacidad: ");
            LocalDate date = readDate("Fecha (YYYY-MM-DD): ");
            EventTags tag = readEventTag();
            Event event = new Event(id, title, capacity, date, tag);
            eventController.addEvent(event);
            System.out.println("Evento creado.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void listEvents() {
        try {
            List<Event> events = eventController.getAllEvents();
            if (events.isEmpty()) {
                System.out.println("No hay eventos registrados.");
                return;
            }
            System.out.println("--- Eventos ---");
            for (Event event : events) {
                printEvent(event);
            }
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void findEventById() {
        try {
            Long id = readLong("ID del evento: ");
            Optional<Event> event = eventController.getEventById(id);
            if (event.isPresent()) {
                printEvent(event.get());
            } else {
                System.out.println("No se encontro el evento.");
            }
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void findEventByTitle() {
        try {
            String title = readRequired("Titulo: ");
            Optional<Event> event = eventController.getEventByTitle(title);
            if (event.isPresent()) {
                printEvent(event.get());
            } else {
                System.out.println("No se encontro el evento.");
            }
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void updateEvent() {
        try {
            Long id = readLong("ID del evento: ");
            String title = readRequired("Nuevo titulo: ");
            int capacity = readInt("Nueva capacidad: ");
            LocalDate date = readDate("Nueva fecha (YYYY-MM-DD): ");
            EventTags tag = readEventTag();
            Event event = new Event(id, title, capacity, date, tag);
            eventController.updateEvent(event);
            System.out.println("Evento actualizado.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void deleteEvent() {
        try {
            Long id = readLong("ID del evento: ");
            eventController.removeEvent(id);
            System.out.println("Evento eliminado.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void createReservation() {
        try {
            Long id = readLong("ID de la reserva: ");
            Long eventId = readLong("ID del evento: ");
            String email = readRequired("Correo electronico: ");
            int seats = readInt("Asientos: ");
            ReservationStatus status = readReservationStatus();
            LocalDate date = readDate("Fecha de la reserva (YYYY-MM-DD): ");
            Reservation reservation = new Reservation(id, eventId, email, seats, status, date);
            reservationController.addReservation(reservation);
            System.out.println("Reserva creada.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void listReservations() {
        try {
            List<Reservation> reservations = reservationController.getAllReservations();
            if (reservations.isEmpty()) {
                System.out.println("No hay reservas registradas.");
                return;
            }
            System.out.println("--- Reservas ---");
            for (Reservation reservation : reservations) {
                printReservation(reservation);
            }
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void findReservationById() {
        try {
            Long id = readLong("ID de la reserva: ");
            Optional<Reservation> reservation = reservationController.getReservationById(id);
            if (reservation.isPresent()) {
                printReservation(reservation.get());
            } else {
                System.out.println("No se encontro la reserva.");
            }
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void updateReservation() {
        try {
            Long id = readLong("ID de la reserva: ");
            Long eventId = readLong("Nuevo ID de evento: ");
            String email = readRequired("Nuevo correo electronico: ");
            int seats = readInt("Nuevos asientos: ");
            ReservationStatus status = readReservationStatus();
            LocalDate date = readDate("Nueva fecha (YYYY-MM-DD): ");
            Reservation reservation = new Reservation(id, eventId, email, seats, status, date);
            reservationController.updateReservation(reservation);
            System.out.println("Reserva actualizada.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void deleteReservation() {
        try {
            Long id = readLong("ID de la reserva: ");
            reservationController.removeReservation(id);
            System.out.println("Reserva eliminada.");
        } catch (InvalidReservationException e) {
            printError(e);
        }
    }

    private void printEvent(Event event) {
        System.out.println("ID: " + event.getId()
                + " | Titulo: " + event.getTitle()
                + " | Capacidad disponible: " + event.getCapacity()
                + " | Fecha: " + event.getDate()
                + " | Tag: " + event.getTag());
    }

    private void printReservation(Reservation reservation) {
        System.out.println("ID: " + reservation.getId()
                + " | Evento: " + reservation.getEventId()
                + " | Email: " + reservation.getCustomerEmail()
                + " | Asientos: " + reservation.getSeats()
                + " | Estado: " + reservation.getStatus()
                + " | Fecha: " + reservation.getDate());
    }

    private int readInt(String prompt) {
        while (true) {
            String input = readRequired(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ser un numero entero.");
            }
        }
    }

    private Long readLong(String prompt) {
        while (true) {
            String input = readRequired(prompt);
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Debe ser un numero entero.");
            }
        }
    }

    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("La entrada no puede estar vacia.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String input = readRequired(prompt);
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida. Formato esperado YYYY-MM-DD.");
            }
        }
    }

    private EventTags readEventTag() {
        while (true) {
            System.out.println("Tags disponibles: " + String.join(", ", enumNames(EventTags.values())));
            String input = readRequired("Tag: ").toUpperCase();
            try {
                return EventTags.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Tag invalido.");
            }
        }
    }

    private ReservationStatus readReservationStatus() {
        while (true) {
            System.out.println("Estados disponibles: " + String.join(", ", enumNames(ReservationStatus.values())));
            String input = readRequired("Estado: ").toUpperCase();
            try {
                return ReservationStatus.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Estado invalido.");
            }
        }
    }

    private String[] enumNames(Enum<?>[] values) {
        return Arrays.stream(values).map(Enum::name).toArray(String[]::new);
    }

    private void printError(InvalidReservationException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
