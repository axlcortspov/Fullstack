# ShopFlow — Plataforma de E-Commerce con Microservicios

**Grupo 10 · DSY1103 Desarrollo FullStack 1 · Duoc UC 2026**  
**Integrante:** Axel Cortés

---

## Descripción del proyecto

ShopFlow es una plataforma de e-commerce construida sobre una arquitectura de microservicios. El sistema permite gestionar el ciclo completo de una compra en línea: registro de usuarios, catálogo de productos, carrito de compras, órdenes, pagos, envíos, notificaciones, reseñas e inventario.

Cada microservicio es independiente, tiene su propia base de datos y se comunica con otros servicios mediante **Feign Client** (Spring Cloud OpenFeign).

---

## Arquitectura del sistema

El sistema está compuesto por **10 microservicios**, cada uno corriendo en su propio contenedor Docker, más un **Eureka Server** para el descubrimiento dinámico de servicios:

| Servicio | Puerto | Descripción |
|---|---|---|
| **eureka-server** | **8761** | **Netflix Eureka — Service Registry** |
| auth-service | 8081 | Registro e inicio de sesión |
| user-service | 8082 | Gestión de usuarios |
| product-service | 8083 | Catálogo de productos |
| cart-service | 8084 | Carrito de compras |
| order-service | 8085 | Órdenes de compra |
| payment-service | 8086 | Pagos |
| inventory-service | 8087 | Control de stock |
| notification-service | 8088 | Notificaciones |
| review-service | 8089 | Reseñas de productos |
| shipping-service | 8090 | Envíos y seguimiento |

### Dependencias entre servicios (Feign Client)

```
cart-service          → user-service, product-service
order-service         → user-service
payment-service       → order-service
shipping-service      → order-service
notification-service  → user-service
review-service        → user-service, product-service
inventory-service     → product-service
```

`auth-service`, `user-service` y `product-service` no consumen servicios externos.

---

## Funcionalidades implementadas

- CRUD completo en los 10 microservicios
- **Eureka Server (Netflix Eureka)** para descubrimiento dinámico de servicios
- **Service Discovery**: todos los microservicios se registran automáticamente en Eureka al iniciar
- Comunicación entre servicios via **Feign Client** con descubrimiento dinámico y logs SLF4J
- **DTOs** de entrada y salida en todos los servicios (no se exponen entidades directamente)
- **Bean Validation** (`@NotNull`, `@NotBlank`, `@Positive`, `@Min`) con respuestas 400 estructuradas
- **Manejo de errores centralizado** con `@RestControllerAdvice` y códigos HTTP correctos (200, 201, 400, 404, 503)
- **Resiliencia**: si un servicio dependiente no está disponible, se retorna 503 en lugar de un error genérico
- Endpoints enriquecidos (`/detail`) que combinan datos de múltiples servicios
- Base de datos MySQL compartida con esquemas separados por servicio
- Despliegue completo con **Docker Compose**

---

## Requisitos previos

- Docker y Docker Compose instalados
- Puerto 8081–8090 disponibles

---

## Pasos para ejecutar

**1. Clonar el repositorio**
```bash
git clone https://github.com/axlcortspov/Fullstack.git
cd Fullstack/servicios
```

**2. Levantar todos los servicios**
```bash
docker compose up -d --build
```

**3. Verificar que todos los contenedores estén corriendo**
```bash
docker ps
```

**4. Ver logs de un servicio específico**
```bash
docker logs order_service -f
```

**5. Bajar todos los servicios**
```bash
docker compose down
```

> Las bases de datos se crean automáticamente con las configuraciones en `docker-compose.yml`. Cada servicio usa Hibernate con `ddl-auto=update` para crear las tablas automáticamente al iniciar.

---

## Endpoints principales

### auth-service :8081
| Método | Endpoint | Descripción |
|---|---|---|
| POST | /auth/register | Registrar usuario |
| POST | /auth/login | Iniciar sesión |

### user-service :8082
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /users | Listar usuarios |
| GET | /users/{id} | Buscar por ID |
| POST | /users | Crear usuario |
| DELETE | /users/{id} | Eliminar usuario |

### product-service :8083
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /products | Listar productos |
| GET | /products/{id} | Buscar por ID |
| POST | /products | Crear producto |
| DELETE | /products/{id} | Eliminar producto |

### cart-service :8084
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /cart | Listar carrito |
| GET | /cart/user/{userId} | Carrito de un usuario |
| GET | /cart/{id}/detail | Item con datos de usuario y producto |
| POST | /cart | Agregar al carrito |
| PUT | /cart/{id} | Actualizar item |
| DELETE | /cart/{id} | Eliminar item |

### order-service :8085
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /orders | Listar órdenes |
| GET | /orders/{id} | Buscar por ID |
| GET | /orders/user/{userId} | Órdenes de un usuario |
| GET | /orders/{id}/detail | Orden con datos del usuario |
| POST | /orders | Crear orden |
| PUT | /orders/{id}/status | Cambiar estado |
| DELETE | /orders/{id} | Eliminar orden |

### payment-service :8086
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /payments | Listar pagos |
| GET | /payments/order/{orderId} | Pagos por orden |
| GET | /payments/{id}/detail | Pago con datos de la orden |
| POST | /payments | Crear pago |
| PUT | /payments/{id} | Actualizar pago |
| PUT | /payments/{id}/status | Cambiar estado |
| DELETE | /payments/{id} | Eliminar pago |

### inventory-service :8087
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /inventory | Listar inventario |
| GET | /inventory/product/{productId} | Stock por producto |
| GET | /inventory/{id}/detail | Inventario con datos del producto |
| POST | /inventory | Crear inventario |
| PUT | /inventory/{id} | Actualizar inventario |
| PUT | /inventory/{id}/stock | Actualizar stock |
| DELETE | /inventory/{id} | Eliminar inventario |

### notification-service :8088
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /notifications | Listar notificaciones |
| GET | /notifications/user/{userId} | Notificaciones por usuario |
| GET | /notifications/{id}/detail | Notificación con datos del usuario |
| POST | /notifications | Crear notificación |
| PUT | /notifications/{id} | Actualizar notificación |
| PUT | /notifications/{id}/status | Cambiar estado |
| DELETE | /notifications/{id} | Eliminar notificación |

### review-service :8089
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /reviews | Listar reseñas |
| GET | /reviews/product/{productId} | Reseñas por producto |
| GET | /reviews/user/{userId} | Reseñas por usuario |
| GET | /reviews/{id}/detail | Reseña con usuario y producto |
| POST | /reviews | Crear reseña |
| PUT | /reviews/{id} | Actualizar reseña |
| DELETE | /reviews/{id} | Eliminar reseña |

### shipping-service :8090
| Método | Endpoint | Descripción |
|---|---|---|
| GET | /shipping | Listar envíos |
| GET | /shipping/order/{orderId} | Envíos por orden |
| GET | /shipping/{id}/detail | Envío con datos de la orden |
| POST | /shipping | Crear envío |
| PUT | /shipping/{id} | Actualizar envío |
| PUT | /shipping/{id}/status | Cambiar estado |
| DELETE | /shipping/{id} | Eliminar envío |

---

## Eureka Server — Descubrimiento de Servicios

### Acceso al dashboard de Eureka

Una vez que todos los servicios están corriendo, puedes acceder al dashboard de **Eureka Server** en:

**http://localhost:8761**

El dashboard muestra:
- **Instancias registradas**: lista de todos los microservicios activos
- **Estado de cada servicio**: si están UP, DOWN o en otro estado
- **Información de instancias**: hostname, puerto, URL de status
- **Replicación entre servidores** (si hay múltiples instancias de Eureka)

### Verificar que los servicios estén registrados

Cuando todos los contenedores inician, cada microservicio:
1. Se conecta automáticamente al Eureka Server en `http://eureka-server:8761/eureka`
2. Se registra con su nombre: `AUTH-SERVICE`, `USER-SERVICE`, `PRODUCT-SERVICE`, etc.
3. Envía heartbeats cada 30 segundos para indicar que sigue activo
4. Se marca como `UP` (disponible) en el dashboard

Si un servicio se detiene, Eureka lo marca como `DOWN` después de algunos heartbeats no recibidos.

### Dependencias entre servicios (Feign Client)

```
cart-service          → user-service, product-service
order-service         → user-service
payment-service       → order-service
shipping-service      → order-service
notification-service  → user-service
review-service        → user-service, product-service
inventory-service     → product-service
```

`auth-service`, `user-service` y `product-service` no consumen servicios externos.

---

## Eureka Server — Descubrimiento de Servicios

### Acceso al dashboard de Eureka

Una vez que todos los servicios están corriendo, puedes acceder al dashboard de **Eureka Server** en:

**http://localhost:8761**

El dashboard muestra:
- **Instancias registradas**: lista de todos los microservicios activos
- **Estado de cada servicio**: si están UP, DOWN o en otro estado
- **Información de instancias**: hostname, puerto, URL de status
- **Replicación entre servidores** (si hay múltiples instancias de Eureka)

### Verificar que los servicios estén registrados

Cuando todos los contenedores inician, cada microservicio:
1. Se conecta automáticamente al Eureka Server en `http://eureka-server:8761/eureka`
2. Se registra con su nombre: `AUTH-SERVICE`, `USER-SERVICE`, `PRODUCT-SERVICE`, etc.
3. Envía heartbeats cada 30 segundos para indicar que sigue activo
4. Se marca como `UP` (disponible) en el dashboard

Si un servicio se detiene, Eureka lo marca como `DOWN` después de algunos heartbeats no recibidos.

---

## Evaluación Parcial 3 (EP3) — Semana 14-15

Los siguientes **5 microservicios** cuentan con tests unitarios en las 4 capas (modelo, servicio, controlador, repositorio):

| Servicio | ModelTest | ServiceTest | ControllerTest | RepositoryTest | Swagger | Estado |
|---|---|---|---|---|---|---|
| **product-service** | HECHO | HECHO | HECHO | HECHO | HECHO | Completo |
| **user-service** | HECHO | HECHO | HECHO | HECHO | HECHO | Completo |
| **order-service** | HECHO | HECHO | HECHO | HECHO | HECHO | Completo |
| **cart-service** | HECHO | HECHO | HECHO | HECHO | HECHO | Completo |
| **payment-service** | HECHO | HECHO | HECHO | HECHO | HECHO | Completo |

### Acceso a documentación Swagger

Cada servicio expone su documentación OpenAPI/Swagger en los siguientes endpoints:

- **product-service** (8083): http://localhost:8083/swagger-ui.html
- **user-service** (8082): http://localhost:8082/swagger-ui.html
- **order-service** (8085): http://localhost:8085/swagger-ui.html
- **cart-service** (8084): http://localhost:8084/swagger-ui.html
- **payment-service** (8086): http://localhost:8086/swagger-ui.html

**API Gateway centralizado** (puerto 8000):
- Swagger UI consolidado: http://localhost:8000/swagger-ui.html
- Rutas configuradas hacia todos los 10 servicios

### Ejecutar tests

Para ejecutar los tests unitarios en cualquier servicio:

```bash
cd servicios/{servicio-name}
./mvnw clean test
```

Ejemplo:
```bash
cd servicios/product-service
./mvnw clean test
```

Resultado esperado: `BUILD SUCCESS` con todos los tests pasando.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.4.5
- Spring Cloud OpenFeign
- **Netflix Eureka Server** (Service Discovery)
- Spring Data JPA + Hibernate
- Bean Validation (jakarta.validation)
- **JUnit 5** + **Mockito** (testing)
- **SpringDoc OpenAPI 2.8.9** (Swagger/OpenAPI)
- MySQL 8.0
- Docker + Docker Compose
- Lombok
- SLF4J (logs)
