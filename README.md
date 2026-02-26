# Práctica Carrito de Compras — API REST con Persistencia

## Descripción del Proyecto

Este proyecto implementa una API REST para gestionar carritos de compra con persistencia en base de datos H2. La aplicación sigue una arquitectura por capas (Controlador → Servicio → Repositorio → Base de datos).

## Modelo de Datos

### Entidad `Carrito`

| Campo          | Tipo     | Descripción                          |
|----------------|----------|--------------------------------------|
| `idCarrito`    | Long     | Identificador único                  |
| `idUsuario`    | Long     | Identificador del usuario            |
| `emailUsuario` | String   | Correo electrónico del usuario       |
| `totalPrecio`  | double   | Total del precio del carrito         |

### Entidad `LineaCarrito`

| Campo            | Tipo     | Descripción                                      |
|------------------|----------|--------------------------------------------------|
| `idLineaCarrito` | Long     | Identificador único                              |
| `carrito_id`     | Long     | FK al carrito al que pertenece                   |
| `idArticulo`     | Long     | Identificador del artículo                       |
| `precioUnitario` | double   | Precio unitario del artículo                     |
| `numUnidades`    | int      | Número de unidades                               |
| `costeLinea`     | double   | Coste de la línea (precioUnitario × numUnidades) |

### Relación

- Un **Carrito** tiene muchas **LineaCarrito** (relación `@OneToMany`).
- Cada **LineaCarrito** pertenece a un único **Carrito** (relación `@ManyToOne`).

## Endpoints de la API REST

### Carritos

| Método   | Ruta                      | Descripción                       | Posibles Respuestas                                                       |
|----------|---------------------------|-----------------------------------|---------------------------------------------------------------------------|
| `POST`   | `/api/carritos`           | Crear un nuevo carrito            | **200 OK** / **400 BAD REQUEST**                                          |
| `GET`    | `/api/carritos`           | Listar todos los carritos         | **200 OK**                                                                |
| `GET`    | `/api/carritos/{id}`      | Consultar un carrito por ID       | **200 OK** / **404 NOT FOUND**                                            |
| `PUT`    | `/api/carritos/{id}`      | Modificar un carrito existente    | **200 OK** / **404 NOT FOUND**                                            |
| `DELETE` | `/api/carritos/{id}`      | Eliminar un carrito por ID        | **200 OK** / **404 NOT FOUND**                                            |

### Líneas de Carrito

| Método   | Ruta                                | Descripción                            | Posibles Respuestas                    |
|----------|-------------------------------------|----------------------------------------|----------------------------------------|
| `POST`   | `/api/carritos/{id}/lineas`         | Añadir una línea a un carrito          | **200 OK** / **404 NOT FOUND**         |
| `GET`    | `/api/carritos/{id}/lineas`         | Consultar líneas de un carrito         | **200 OK** / **404 NOT FOUND**         |
| `PUT`    | `/api/lineas/{idLinea}`             | Modificar una línea de carrito         | **200 OK** / **404 NOT FOUND**         |
| `DELETE` | `/api/lineas/{idLinea}`             | Eliminar una línea de carrito          | **200 OK** / **404 NOT FOUND**         |

### Ejemplos de cuerpo JSON

**Crear carrito:**
```json
{
  "idUsuario": 1,
  "emailUsuario": "usuario@example.com"
}
```

**Añadir línea a carrito:**
```json
{
  "idArticulo": 101,
  "precioUnitario": 10.50,
  "numUnidades": 3
}
```


### Capas

1. **Controlador REST** (`ControladorREST`): Recibe las peticiones HTTP y delega al servicio.
2. **Servicio** (`ServicioCarrito`): Contiene la lógica de negocio (crear, modificar, eliminar, recalcular totales).
3. **Repositorio** (`RepoCarrito`, `RepoLineaCarrito`): Interfaces que extienden `CrudRepository` para comunicarse con la base de datos.
4. **Entidades** (`Carrito`, `LineaCarrito`): Clases JPA que representan las tablas de la base de datos.

La API estará disponible en `http://localhost:8080/api/`.