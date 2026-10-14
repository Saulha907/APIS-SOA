# API REST de Apartamentos

Proyecto académico para administrar alquileres de propiedades y habitaciones mediante una API REST desarrollada con Java, Spring Boot y MySQL. La API permite registrar clientes, propiedades y unidades rentables; establecer periodos de disponibilidad; crear reservaciones; registrar pagos simulados; publicar reseñas; asociar URLs de imágenes; y administrar notificaciones.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA y Jakarta Validation
- MySQL
- Maven Wrapper (`mvnw.cmd`)
- Postman para las pruebas de API
- BCrypt disponible mediante `spring-security-crypto`; verificar que todas las rutas de escritura de clientes apliquen el hash

## Requisitos

Java 21, MySQL activo, una base de datos llamada `apartamentos_blue` y acceso a una terminal en la raíz del proyecto.

## Configuración y ejecución

1. Crear la base de datos en MySQL:

   ```sql
   CREATE DATABASE IF NOT EXISTS apartamentos_blue;
   ```

2. Comprobar que `src/main/resources/application.properties` **no contenga contraseñas reales**. Configuración de ejemplo:

   ```properties
   spring.application.name=apartamentos
   server.port=8000
   spring.datasource.url=jdbc:mysql://localhost:3306/apartamentos_blue?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=${DB_USER:root}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.open-in-view=false
   ```

3. En PowerShell, definir las variables para la sesión actual e iniciar:

   ```powershell
   $env:DB_USER = "root"
   $env:DB_PASSWORD = Read-Host "Contraseña de MySQL"
   .\mvnw.cmd clean spring-boot:run
   ```

4. API disponible en `http://localhost:8000`. Para probarla, abrir Postman y usar `Content-Type: application/json` en las solicitudes con cuerpo JSON.

> `ddl-auto=update` es práctico para desarrollo académico; no equivale a un sistema de migraciones para producción.

## Módulos y endpoints

Los endpoints usan como prefijo `http://localhost:8000`.

| Módulo | Recurso base | Operaciones |
|---|---|---|
| Clientes | `/api/clientes` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Propiedades | `/api/propiedades` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Unidades rentables | `/api/unidades` | GET lista, GET `/{id}`, GET `/propiedad/{idPropiedad}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Disponibilidades | `/api/disponibilidades` | GET lista, GET `/{id}`, GET `/unidad/{idUnidad}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Reservaciones | `/api/reservas` | GET lista, GET `/{id}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Pagos simulados | `/api/pagos` | GET lista, GET `/{id}`, GET `/reserva/{idReserva}`, POST, PUT `/{id}`, DELETE `/{id}`, POST `/{id}/confirmar`, POST `/{id}/reembolsar` |
| Reseñas | `/api/resenas` | GET lista, GET `/{id}`, GET `/reserva/{idReserva}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Imágenes | `/api/propiedad-imagenes` | GET lista, GET `/{id}`, GET `/propiedad/{idPropiedad}`, POST, PUT `/{id}`, DELETE `/{id}` |
| Notificaciones | `/api/notificaciones` | GET lista, GET `/{id}`, GET `/destinatario/{idCliente}`, POST, PUT `/{id}`, DELETE `/{id}` |

Los IDs de los ejemplos son ilustrativos: dependen de los registros existentes en la base de datos.

## Ejemplos de solicitudes y respuestas

### Crear un cliente

`POST /api/clientes`

```json
{
  "tipo": "CLIENTE",
  "nombres": "Ana",
  "apellidos": "García",
  "email": "ana.ejemplo@example.com",
  "telefono": "2295550000",
  "fechaNacimiento": "1999-02-15",
  "ine": "INEFICTICIA001",
  "direccion": "Veracruz, México",
  "estatus": "ACTIVO",
  "password": "ClaveSoloDePrueba123"
}
```

Respuesta orientativa: `200 OK` con un identificador generado; el campo `password` no debe mostrarse en el JSON de respuesta.

### Registrar disponibilidad

`POST /api/disponibilidades`

```json
{
  "unidad": { "idUnidad": 1 },
  "fechaInicio": "2026-11-20T14:00:00",
  "fechaFin": "2026-11-25T12:00:00",
  "disponible": true,
  "precioEspecial": 500.00
}
```

Respuesta orientativa: `201 Created` con `idDisponibilidad` y los datos registrados.

### Crear una reservación

`POST /api/reservas`

```json
{
  "unidad": { "idUnidad": 1 },
  "cliente": { "id": 1 },
  "fechaEntrada": "2026-11-20T14:00:00",
  "fechaSalida": "2026-11-25T12:00:00",
  "estado": "PENDIENTE",
  "comentario": "Reservación de prueba"
}
```

Si hay disponibilidad, no existe otra reserva ocupando el intervalo y la unidad está habilitada, la respuesta esperada es `201 Created`. El servidor calcula `importeTotal` con el precio aplicable y la cantidad de noches. Para cinco noches a $500, el importe es `$2,500 MXN`. Las reservas nuevas comienzan como `PENDIENTE`.

### Confirmar una reservación existente

`PUT /api/reservas/4`

```json
{
  "estado": "CONFIRMADA",
  "comentario": "Reservación confirmada"
}
```

Respuesta esperada para una reserva pendiente existente: `200 OK`, conservando precio, fechas, cliente y unidad. Una reserva no puede pasar directamente de `PENDIENTE` a `FINALIZADA`; tampoco puede finalizar antes de su fecha de salida.

### Crear y confirmar un pago simulado

`POST /api/pagos`

```json
{
  "reserva": { "idReservacion": 4 },
  "importe": 2500.00,
  "fechaVencimiento": "2026-11-19",
  "metodo": "TRANSFERENCIA",
  "estado": "PENDIENTE"
}
```

El servicio obtiene el cliente desde la reservación y genera la referencia automáticamente. Para confirmar un pago existente: `POST /api/pagos/{id}/confirmar`, sin body. Para marcarlo como reembolsado: `POST /api/pagos/{id}/reembolsar`, sin body. **No se ejecutan cobros ni reembolsos bancarios reales.**

### Ejemplo de error de validación

Una reservación fuera de los periodos disponibles devuelve `400 Bad Request` con una respuesta de este tipo:

```json
{
  "status": 400,
  "error": "La unidad no tiene disponibilidad en las fechas solicitadas"
}
```

Otros códigos habituales: `200 OK` para consultas y actualizaciones, `201 Created` para creaciones, `204 No Content` para eliminaciones y `404 Not Found` cuando un ID no existe. La respuesta exacta depende del endpoint.

## Reglas funcionales comprobadas con Postman

- Se impiden reservaciones superpuestas en una misma unidad.
- Se rechazan fechas sin disponibilidad registrada.
- El servidor calcula el precio a partir de la disponibilidad y la unidad, ignorando importes arbitrarios en la creación.
- La actualización de una reserva conserva el importe previamente acordado y controla los cambios de estado.
- No se permite finalizar una estancia futura.
- Los pagos pueden pasar de pendientes a pagados y a reembolsados mediante endpoints específicos.
- Las reseñas aceptan puntuaciones de 1 a 5; verifican cliente, estado finalizado, fecha de salida y ausencia de reseñas duplicadas.
- Las notificaciones pueden pasar de `NO_LEIDA` a `LEIDA` sin perder su fecha original.

## Limitaciones y mejoras pendientes

- Proyecto académico **sin autenticación ni autorización de usuarios**. No exponer directamente en Internet.
- El módulo de pagos simula estados contables; no hay pasarela bancaria.
- Las imágenes almacenan URLs, no archivos binarios.
- Verificar en todas las rutas de escritura de clientes que las contraseñas se almacenen mediante hash y no en texto plano; no usar datos personales reales.
- Completar pruebas automatizadas, concurrencia de reservaciones, reglas de periodos solapados y restricciones de borrado de registros asociados.
- La lógica de negocio descrita considera los cambios realizados después del último ZIP compartido; **actualizar los archivos fuente locales antes de publicar la versión definitiva**.

## Evidencias y entrega

Adjuntar capturas de Postman con operaciones satisfactorias y errores esperados: creación/consulta de entidades, `400` por reserva sin disponibilidad, `400` por fechas ocupadas, cálculo del importe, `400` por cambio de estado no permitido, `400` por reseña duplicada, confirmación/reembolso de pago y notificación marcada como leída.

https://github.com/Saulha907/APIS-SOA.git