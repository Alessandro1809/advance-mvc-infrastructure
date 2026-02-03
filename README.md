# Reservation Manager (MVC)

Proyecto de consola en Java para administrar eventos y reservas aplicando el patrón MVC, validaciones de negocio y manejo de excepciones. Todo el almacenamiento es en memoria.

**Estado actual**
- Gestión completa de eventos y reservas desde menú por consola.
- Capacidad del evento se interpreta como asientos disponibles.
- Reservas `PENDING` y `CONFIRMED` descuentan asientos disponibles.
- Reservas `CANCELLED` no descuentan asientos.

**Lo que pone en práctica**
- Patrón MVC con separación clara de responsabilidades.
- Capas de Controller, Service, Repository y Model.
- Validaciones de dominio y manejo de errores con excepciones personalizadas.
- Uso de `Optional` para evitar `null` en consultas.
- Reglas de negocio aplicadas en la capa de servicio.
- Persistencia en memoria usando listas (`List`).
- Uso de `enum` para estados y categorías.
- Logging básico en servicios (`java.util.logging`).
- Lombok para reducir boilerplate (getters, setters, constructores).

**Tecnologías**
- Java
- Maven
- Lombok

**Requisitos**
- JDK compatible con la configuración del proyecto (`maven.compiler.source/target`).
- Maven instalado si ejecutas por terminal.
- Si usas IDE: plugin de Lombok y annotation processing habilitado.

**Ejecución**
1. Abre el proyecto en tu IDE (IntelliJ recomendado).
2. Verifica que el SDK del proyecto coincida con el configurado en `pom.xml`.
3. Ejecuta `Main` como aplicación.

Si deseas ejecutar desde terminal:
1. Asegúrate de que la clase `Main` tenga la firma estándar `public static void main(String[] args)`.
2. Compila con Maven.
3. Ejecuta con `java -cp target/classes dev.ale.proyect.reservation.Main`.

**Arquitectura (MVC + capas)**
- **Model**: `Event`, `Reservation` y enums de dominio.
- **View**: `View` implementa el menú por consola y captura inputs.
- **Controller**: `EventController`, `ReservationController` orquestan llamadas a servicios.
- **Service**: `EventService`, `ReservationService` aplican reglas de negocio.
- **Repository**: `EventsRepository`, `ReservationRepository` almacenan datos en memoria.
- **Exception**: `InvalidReservationException` centraliza errores de validación/negocio.

**Modelo de datos**
- **Event**: `id` (identificador único), `title` (título), `capacity` (asientos disponibles), `date` (fecha), `tag` (categoría).
- **Reservation**: `id` (identificador único), `eventId` (evento asociado), `customerEmail` (correo), `seats` (asientos), `status` (estado), `date` (fecha).

**Enums**
- `EventTags`: `JAVA`, `WEB`, `DEVOPS`, `DATA`, `OTHER`.
- `ReservationStatus`: `PENDING`, `CONFIRMED`, `CANCELLED`.

**Reglas de negocio (eventos)**
- El evento no puede ser nulo.
- `title` no puede ser nulo ni vacío.
- `capacity` debe ser mayor a 0.
- `date` no puede ser nula ni una fecha pasada.
- `id` debe ser mayor a 0.
- No se permite crear un evento con `id` ya existente.

**Reglas de negocio (reservas)**
- La reserva no puede ser nula.
- `seats` debe ser mayor a 0.
- `date` no puede ser nula ni una fecha pasada.
- `customerEmail` debe contener `@` y no estar vacío.
- `status` no puede ser nulo.
- `id` debe ser mayor a 0.
- No se puede reservar si el evento no existe.
- Al crear una reserva `PENDING` o `CONFIRMED` se valida disponibilidad contra la capacidad disponible y se descuentan asientos del evento.
- Al actualizar una reserva se reequilibra la capacidad: si la reserva anterior descontaba asientos se devuelven, y si la reserva nueva descuenta asientos se vuelven a descontar.
- Al eliminar una reserva se devuelven asientos si la reserva descontaba (PENDING o CONFIRMED).

**Flujo de uso (menú de consola)**
1. Eventos
2. Reservas
3. Salir

**Ejemplos de uso (paso a paso con fechas reales)**
Ejemplo 1: crear evento y reservar asientos (PENDING descuenta capacidad).
1. Entra a `Eventos` -> `Crear evento`.
2. Ingresa `ID=1`, `Titulo=Java Conf`, `Capacidad=5`, `Fecha=2026-03-15`, `Tag=JAVA`.
3. Entra a `Reservas` -> `Crear reserva`.
4. Ingresa `ID=10`, `Evento=1`, `Correo=test@mail.com`, `Asientos=2`, `Estado=PENDING`, `Fecha=2026-03-01`.
5. Lista eventos y verifica que la capacidad disponible ahora es `3`.

Ejemplo 2: confirmar una reserva y ver descuento.
1. Entra a `Reservas` -> `Actualizar reserva`.
2. Ingresa `ID=10`, `Evento=1`, `Correo=test@mail.com`, `Asientos=2`, `Estado=CONFIRMED`, `Fecha=2026-03-01`.
3. Lista eventos y verifica que la capacidad disponible se mantiene en `3` (ya estaba descontada).

Ejemplo 3: cancelar una reserva y devolver asientos.
1. Entra a `Reservas` -> `Actualizar reserva`.
2. Ingresa `ID=10`, `Evento=1`, `Correo=test@mail.com`, `Asientos=2`, `Estado=CANCELLED`, `Fecha=2026-03-01`.
3. Lista eventos y verifica que la capacidad disponible sube a `5`.

Ejemplo 4: cambiar de evento en una reserva.
1. Crea otro evento con `ID=2`, `Capacidad=4`, `Fecha=2026-04-10`, `Tag=WEB`.
2. Entra a `Reservas` -> `Actualizar reserva`.
3. Ingresa `ID=10`, `Evento=2`, `Correo=test@mail.com`, `Asientos=2`, `Estado=CONFIRMED`, `Fecha=2026-03-01`.
4. Verifica que el evento `1` recupera `2` asientos y el evento `2` descuenta `2`.

**Casos de prueba manuales (Inputs/Outputs)**
| Caso | Entradas | Resultado esperado |
| --- | --- | --- |
| 1 | Crear evento: `ID=1`, `Titulo=Java Conf`, `Capacidad=5`, `Fecha=2026-03-15`, `Tag=JAVA` | Evento creado y visible en el listado |
| 2 | Crear evento: `Capacidad=0` | Error de validación |
| 3 | Crear evento: `Fecha=2025-12-31` | Error de validación por fecha pasada |
| 4 | Crear reserva: `Evento=999` | Error indicando que el evento no existe |
| 5 | Crear reserva `PENDING`: `Evento=1`, `Asientos=2`, `Fecha=2026-03-01` | Reserva creada y capacidad del evento disminuye |
| 6 | Crear reserva `PENDING`: `Evento=1`, `Asientos=99` | Error de disponibilidad |
| 7 | Actualizar reserva `PENDING` a `CONFIRMED` sin cambiar asientos | Capacidad disponible no cambia |
| 8 | Actualizar reserva `PENDING` a `CANCELLED` | Se devuelven asientos al evento |
| 9 | Actualizar reserva aumentando `seats` sin disponibilidad | Error de disponibilidad |
| 10 | Eliminar una reserva `CONFIRMED` | Se devuelven asientos al evento |

**Operaciones de eventos**
- Crear evento.
- Listar eventos.
- Buscar por ID.
- Buscar por título.
- Actualizar evento.
- Eliminar evento.

**Operaciones de reservas**
- Crear reserva.
- Listar reservas.
- Buscar por ID.
- Actualizar reserva.
- Eliminar reserva.

**Estructura del proyecto**
- `src/main/java/dev/ale/proyect/reservation/Main.java`
- `src/main/java/dev/ale/proyect/reservation/view/View.java`
- `src/main/java/dev/ale/proyect/reservation/controller/`
- `src/main/java/dev/ale/proyect/reservation/service/`
- `src/main/java/dev/ale/proyect/reservation/repository/`
- `src/main/java/dev/ale/proyect/reservation/model/`
- `src/main/java/dev/ale/proyect/reservation/interfaces/`
- `src/main/java/dev/ale/proyect/reservation/exception/InvalidReservationException.java`

**Notas importantes**
- El almacenamiento es en memoria, los datos se pierden al cerrar la app.
- La capacidad mostrada es “disponible”, no “total”.
- Lombok requiere soporte en el IDE si editas el proyecto.

**Posibles mejoras**
- Persistencia en base de datos.
- Tests unitarios para servicios y validadores.
- Reportes de ocupación por evento.
- Exportación de reservas a CSV.
