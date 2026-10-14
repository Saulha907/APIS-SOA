# API REST de Biblioteca (Librería)

Proyecto académico desarrollado para la asignatura **Arquitectura Orientada a Servicios**. Permite administrar el catálogo de libros, los usuarios de la biblioteca y el registro y devolución de préstamos mediante una API REST.

## Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- MySQL
- Maven Wrapper
- Postman para realizar pruebas

## Organización del proyecto

El código está organizado en cuatro capas: `models` (entidades), `repositories` (consultas a MySQL), `services` (reglas de negocio) y `controllers` (rutas HTTP).

| Módulo | Función principal |
|---|---|
| Libros | Registrar, consultar, actualizar, eliminar y buscar libros. |
| Usuarios | Administrar los datos de los usuarios de la biblioteca. |
| Préstamos | Registrar préstamos, consultar historiales y procesar devoluciones. |

## Requisitos e instalación

1. Tener instalado **Java 21** y **MySQL**.
2. Crear una base de datos llamada `libreria`:

   ```sql
   CREATE DATABASE IF NOT EXISTS libreria;
   ```

3. Abrir `src/main/resources/application.properties` y **sustituir cualquier contraseña escrita directamente** por variables de entorno. Una configuración recomendada para desarrollo es:

   ```properties
   spring.application.name=libreria
   server.port=1600
   spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/libreria?useSSL=false&serverTimezone=UTC}
   spring.datasource.username=${DB_USER:root}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. Desde PowerShell, en la raíz del proyecto, iniciar el servidor:

   ```powershell
   $env:DB_USER = "root"
   $env:DB_PASSWORD = Read-Host "Contraseña de MySQL"
   .\mvnw.cmd spring-boot:run
   ```

5. Abrir Postman y usar como dirección base **`http://localhost:1600`**. Para crear o actualizar registros, enviar los datos como **Body → raw → JSON**.

> `ddl-auto=update` se utiliza por comodidad en prácticas académicas. No sustituye las migraciones de base de datos en un entorno de producción.

## Endpoints de la API

Las rutas de esta tabla se agregan a `http://localhost:1600`.

### Libros

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/libros` | Consultar todos los libros. |
| GET | `/libros/{isbn}` | Consultar un libro por su identificador. |
| POST | `/libros` | Registrar un libro. |
| PUT | `/libros/{isbn}` | Actualizar los datos de un libro. |
| DELETE | `/libros/{isbn}` | Eliminar un libro. |
| GET | `/libros/autor/{autor}` | Buscar por autor exacto. |
| GET | `/libros/titulo/{titulo}` | Buscar por título exacto. |

### Usuarios

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/usuarios` | Consultar usuarios. |
| GET | `/usuarios/{id}` | Consultar un usuario. |
| POST | `/usuarios` | Registrar un usuario. |
| PUT | `/usuarios/{id}` | Actualizar un usuario. |
| DELETE | `/usuarios/{id}` | Eliminar un usuario. |

### Préstamos

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/prestamos` | Listar préstamos. |
| GET | `/prestamos/{id}` | Consultar un préstamo. |
| GET | `/prestamos/usuario/{usuarioId}` | Consultar préstamos de un usuario. |
| GET | `/prestamos/libro/{isbn}` | Consultar préstamos de un libro. |
| POST | `/prestamos?usuarioId={id}&isbn={isbn}` | Registrar un préstamo, sin Body. |
| PUT | `/prestamos/{id}/devolver` | Registrar una devolución, sin Body. |

`isbn` funciona como identificador interno numérico del libro en esta implementación.

## Ejemplos de uso

### 1. Registrar un libro

**POST** `http://localhost:1600/libros`

```json
{
  "titulo": "El Principito",
  "autor": "Antoine de Saint-Exupery",
  "añoPublicacion": "1943",
  "numeroEjemplares": 5
}
```

En las pruebas del proyecto, el registro devolvió **200 OK** e incluyó el identificador `isbn` generado.

### 2. Registrar un préstamo

**POST** `http://localhost:1600/prestamos?usuarioId=1&isbn=1`

No requiere cuerpo JSON. Es necesario que el libro y el usuario existan y que haya ejemplares disponibles. Cuando se registra, el número de ejemplares disminuye en uno y se asigna la fecha del préstamo.

**Respuesta observada:** `201 Created`. Ejemplo abreviado:

```json
{
  "id": 1,
  "fechaPrestamo": "2026-10-14",
  "fechaDevolucion": null
}
```

### 3. Devolver un libro

**PUT** `http://localhost:1600/prestamos/1/devolver`

No requiere cuerpo JSON. El sistema establece la fecha de devolución y aumenta en uno la cantidad de ejemplares del libro.

**Respuesta observada:** `200 OK`. Si se intenta devolver dos veces el mismo préstamo, responde **400 Bad Request** con el mensaje `Este libro ya fue devuelto`.

## Reglas de negocio implementadas

- No se puede prestar un libro si no tiene ejemplares disponibles.
- No se puede registrar un préstamo con un usuario o libro inexistente.
- Un préstamo reduce en uno el número de ejemplares disponibles.
- Una devolución aumenta en uno ese número.
- Una devolución duplicada se rechaza.
- Los préstamos pueden consultarse por usuario y por libro.

## Pruebas realizadas

Las operaciones de libros, usuarios, préstamos y devoluciones se probaron con Postman. Se verificaron altas, consultas, actualizaciones y eliminaciones de registros, así como el bloqueo de una devolución repetida. Estos resultados **no implican que todos los casos extremos estén cubiertos**.

## Seguridad y limitaciones

**Este proyecto es académico y no debe utilizarse con información real ni publicarse como servicio de producción.** En la versión revisada, las contraseñas de usuarios se almacenan sin hash y pueden incluirse en las respuestas JSON. No hay un sistema de autenticación o autorización implementado. Se recomienda añadir validaciones, almacenamiento seguro de contraseñas y control de concurrencia para préstamos en una versión posterior.

**Antes de subir a GitHub:**

- Quitar del archivo `application.properties` toda contraseña real de MySQL, reemplazándola por `${DB_PASSWORD}`.
- No subir archivos `.env`, configuraciones privadas, datos personales ni archivos compilados.
- Revisar el historial de Git si alguna contraseña fue versionada previamente; ignorar un archivo no borra secretos ya publicados.
- Incluir `README.md`, `pom.xml`, `mvnw`, `mvnw.cmd` y el directorio de código fuente.

## Repositorio
https://github.com/Saulha907/APIS-SOA.git
