# Customer Service

Microservicio encargado de la gestion de `Persona` y `Cliente`. Expone el CRUD de clientes, persiste la informacion en MySQL y publica eventos asincronos para que `account-service` mantenga una referencia local de clientes.

## Que hace esta aplicacion

- administra clientes y sus datos personales
- expone el endpoint `/clientes`
- publica eventos `CLIENTE_UPSERT` y `CLIENTE_DELETE`
- usa JPA con MySQL
- documenta la API con Swagger/OpenAPI
- incluye pruebas unitarias e integracion

## Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL
- RabbitMQ
- MapStruct
- Gradle
- Docker / Docker Compose

## Puertos y URLs importantes

- API: `http://localhost:8081`
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- MySQL: `localhost:3306`
- RabbitMQ AMQP: `localhost:5672`
- RabbitMQ UI: `http://localhost:15672`

Credenciales por defecto de RabbitMQ:

- usuario: `guest`
- clave: `guest`

## Endpoints principales

- `GET /clientes?page=0&size=10`
- `GET /clientes/{clienteId}`
- `POST /clientes`
- `PUT /clientes/{clienteId}`
- `DELETE /clientes/{clienteId}`

## Como ejecutar

Requisito:

- tener Docker Desktop levantado

Desde la raiz del proyecto:

```powershell
docker compose down -v
docker compose up --build
```

Si quieres correr pruebas:

```powershell
.\gradlew test
```

## Ejemplo rapido de prueba

Listar clientes:

```http
GET http://localhost:8081/clientes?page=0&size=10
```

Crear cliente:

```http
POST http://localhost:8081/clientes
Content-Type: application/json
```

```json
{
  "clienteId": 10,
  "nombre": "Cliente Evento",
  "genero": "Femenino",
  "edad": 31,
  "identificacion": "9998887776",
  "direccion": "Calle Rabbit 123",
  "telefono": "3000000000",
  "contrasena": "1234",
  "estado": true
}
```

## Resiliencia y consistencia

Este servicio publica eventos usando patron **outbox**:

- la operacion de negocio guarda primero el cambio del cliente
- en la misma transaccion registra un evento en `outbox_event`
- un proceso programado intenta publicar los eventos pendientes a RabbitMQ
- si RabbitMQ falla, el evento queda pendiente o fallido para reintento posterior

Esto reduce el riesgo de inconsistencias entre base de datos y broker.

## RabbitMQ

- exchange principal: `customer.exchange`
- routing key principal: `customer.sync`
- cola principal: `customer.account.queue`
- dead-letter exchange: `customer.exchange.dlx`
- dead-letter queue: `customer.account.queue.dlq`

## Consistencia eventual

La sincronizacion con `account-service` es eventual:

- un cliente puede quedar guardado inmediatamente en `customer-service`
- segundos despues el evento se publica
- `account-service` consume ese evento y actualiza `clientes_referencia`

Durante esa ventana, `account-service` podria rechazar operaciones sobre un `clienteId` que aun no haya sido sincronizado.

## Base de datos

El esquema inicial esta en [BaseDatos.sql](c:/Users/Alejandra%20Carvajal%20C/Desktop/prueba%20sofka/customer-service/BaseDatos.sql).

Tablas principales:

- `personas`
- `clientes`
- `outbox_event`
