# AlquilaYa — API REST e interfaz web

Proyecto académico de **Arquitectura Orientada a Servicios** para administrar una empresa de alquiler de vehículos. Incluye una interfaz web para trabajar desde el navegador y una API REST para consultar y modificar la información desde Postman u otros clientes.

## Tecnologías

- Java 21 y Spring Boot 4.1.1
- Spring Web MVC, Spring Data JPA (Hibernate) y Spring Security
- MySQL 8 y Maven Wrapper
- HTML, CSS y JavaScript para la interfaz web
- Postman para las pruebas de la API

Arquitectura de capas: `models`, `repositories`, `services` y `controllers`. La información se almacena en MySQL.

## Funcionalidades

| Módulo | Función |
|---|---|
| Clientes | Registrar, consultar, editar y eliminar clientes |
| Oficinas | Administrar sucursales de alquiler |
| Vehículos | Registrar y consultar la flota, sus tarifas y oficina asignada |
| Alquileres | Relacionar vehículos y clientes, calcular automáticamente el total y gestionar estados |
| Mantenimientos | Registrar servicios, costos y kilometraje de los vehículos |
| Usuarios | Inicio de sesión con los roles ADMIN y OPERADOR |

## Requisitos

- JDK 21
- MySQL en funcionamiento
- PowerShell en Windows (o terminal equivalente)
- Puerto `8080` libre, o configurar `SERVER_PORT`

## Instalación y ejecución

**1. Crear la base de datos** en MySQL Workbench:

```sql
CREATE DATABASE IF NOT EXISTS alquilaya
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**2. Configurar variables de entorno** en PowerShell, dentro de la carpeta del proyecto:

```powershell
$env:DB_PASSWORD = Read-Host "Contraseña de MySQL"
$env:ADMIN_PASSWORD = Read-Host "Contraseña de administrador"
$env:OPERADOR_PASSWORD = Read-Host "Contraseña de operador"
```

Opcionales: `DB_URL`, `DB_USER`, `SERVER_PORT`, `ADMIN_EMAIL`, `OPERADOR_EMAIL`.

**3. Iniciar la aplicación:**

```powershell
.\mvnw.cmd clean spring-boot:run
```

Si arranca correctamente, aparecerá `Started AlquilayaApplication`. Con la configuración actual, Hibernate crea o actualiza las tablas mediante `spring.jpa.hibernate.ddl-auto=update`.

**4. Abrir la interfaz:** <http://localhost:8080/>

Iniciar sesión con el correo de administrador (`admin@alquilaya.com` de forma predeterminada) y la contraseña asignada a ese usuario. El operador predeterminado usa `operador@alquilaya.com`.

**Importante:** las variables `ADMIN_PASSWORD` y `OPERADOR_PASSWORD` se usan al crear las cuentas por primera vez; si el usuario ya existe en MySQL, cambiar la variable no cambia automáticamente su contraseña.

## Endpoints REST

Base: `http://localhost:8080`

| Método | Endpoint | Acción |
|---|---|---|
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Consultar cliente |
| POST | `/api/clientes` | Crear cliente |
| PUT | `/api/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/clientes/{id}` | Eliminar cliente |
| GET | `/api/clientes/email/{email}` | Buscar por correo |
| GET | `/api/clientes/licencia/{licencia}` | Buscar por licencia |
| GET | `/api/oficinas` | Listar oficinas |
| GET | `/api/oficinas/{id}` | Consultar oficina |
| POST | `/api/oficinas` | Crear oficina |
| PUT | `/api/oficinas/{id}` | Actualizar oficina |
| DELETE | `/api/oficinas/{id}` | Eliminar oficina |
| GET | `/api/vehiculos` | Listar vehículos |
| GET | `/api/vehiculos/{id}` | Consultar vehículo |
| POST | `/api/vehiculos` | Crear vehículo |
| PUT | `/api/vehiculos/{id}` | Actualizar vehículo |
| DELETE | `/api/vehiculos/{id}` | Eliminar vehículo |
| GET | `/api/alquileres` | Listar alquileres |
| GET | `/api/alquileres/{id}` | Consultar alquiler |
| POST | `/api/alquileres` | Registrar alquiler |
| PUT | `/api/alquileres/{id}/estado` | Cambiar estado de alquiler |
| DELETE | `/api/alquileres/{id}` | Eliminar alquiler |
| GET | `/api/mantenimientos` | Listar mantenimientos |
| GET | `/api/mantenimientos/{id}` | Consultar mantenimiento |
| POST | `/api/mantenimientos` | Crear mantenimiento |
| PUT | `/api/mantenimientos/{id}` | Actualizar mantenimiento |
| DELETE | `/api/mantenimientos/{id}` | Eliminar mantenimiento |

Los endpoints `/api/**` requieren autenticación. En Postman elegir **Authorization → Basic Auth** e introducir el correo y contraseña de un usuario autorizado. Para la interfaz web se utiliza la página de inicio de sesión.

## Ejemplos de solicitudes

Crear cliente — `POST /api/clientes`:

```json
{
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.prueba@example.com",
  "licenciaConducir": "VER12345678",
  "telefono": "2291234567"
}
```

Crear oficina — `POST /api/oficinas`:

```json
{
  "nombre": "AlquilaYa Veracruz",
  "direccion": "Avenida Independencia 100",
  "ciudad": "Veracruz",
  "region": "Veracruz"
}
```

Crear vehículo — `POST /api/vehiculos` (requiere una oficina existente):

```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2023,
  "matricula": "VER123ABC",
  "tarifaDiaria": 850.00,
  "estado": "DISPONIBLE",
  "oficina": { "idOficina": 1 }
}
```

Crear alquiler — `POST /api/alquileres` (requiere cliente y vehículo existentes):

```json
{
  "vehiculo": { "idVehiculo": 1 },
  "cliente": { "idCliente": 1 },
  "fechaAlquiler": "2026-10-20",
  "fechaDevolucion": "2026-10-23",
  "estado": "PENDIENTE"
}
```

**Respuesta observada en Postman:** `201 Created`, `idAlquiler: 1` y `precioTotal: 2550.00` (3 días × 850.00). El servidor calcula el precio automáticamente.

Registrar mantenimiento — `POST /api/mantenimientos`:

```json
{
  "vehiculo": { "idVehiculo": 1 },
  "fechaMantenimiento": "2026-10-10",
  "descripcion": "Cambio de aceite y revision general",
  "costo": 1200.00,
  "kilometraje": 35000
}
```

## Pruebas y códigos de respuesta

Se comprobaron el arranque de Spring Boot, la conexión a MySQL, el acceso autenticado, las consultas de clientes y el registro de clientes, oficinas, vehículos, alquileres y mantenimientos mediante Postman (`200 OK` / `201 Created`). También se comprobó desde la interfaz web la consulta, creación, edición y eliminación de clientes.

Los códigos habituales son `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `401 Unauthorized` y `404 Not Found`. No se han probado exhaustivamente todas las combinaciones de error ni todas las acciones de la interfaz.

## Seguridad y notas de entrega

- No publicar contraseñas de MySQL ni de las cuentas de acceso.
- Mantener `application.properties` usando variables de entorno como `${DB_PASSWORD}`, `${ADMIN_PASSWORD}` y `${OPERADOR_PASSWORD}`.
- No subir `.env`, archivos de configuración local con secretos ni el directorio `target/`.
- El proyecto es académico. Antes de desplegarlo públicamente, revisar autorización por operación, protección CSRF (desactivada en la configuración actual), concurrencia, integridad de datos y reglas de alquiler.
- Los ejercicios de nivelación de POO del PDF son independientes de esta API.

## Repositorio y documentación

https://github.com/Saulha907/APIS-SOA.git

La documentación académica en Word contiene la descripción, tecnologías, tabla de endpoints, ejemplos y capturas de Postman y de la interfaz web.
