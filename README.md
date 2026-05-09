# Proyecto Semestral: ShopFlow

## Integrante
- Axel Cortés

## Descripción del Sistema

**ShopFlow** es un sistema de comercio electrónico basado en arquitectura de microservicios.  
El sistema permite gestionar usuarios, autenticación, productos, carrito de compras, órdenes, pagos, inventario, notificaciones, reseñas y envíos.

Cada microservicio posee su propia responsabilidad, su propia API REST y su propia base de datos.  
La comunicación entre servicios se realiza mediante **Feign Client**, respetando la regla de que un microservicio nunca debe acceder directamente a la base de datos de otro.

---

## Estado del Sistema

| Microservicio        | Puerto | DB Name          | Funcionalidad                         |
| :------------------- | :----- | :--------------- | :------------------------------------ |
| Auth-Service         | 8081   | auth_db          | Registro y Login                      |
| User-Service         | 8082   | user_db          | Gestión de usuarios                   |
| Product-Service      | 8083   | product_db       | Gestión de productos                  |
| Cart-Service         | 8084   | cart_db          | Gestión de carrito de compras         |
| Order-Service        | 8085   | order_db         | Gestión de órdenes                    |
| Payment-Service      | 8086   | payment_db       | Gestión de pagos                      |
| Inventory-Service    | 8087   | inventory_db     | Control de stock                      |
| Notification-Service | 8088   | notification_db  | Gestión de notificaciones             |
| Review-Service       | 8089   | review_db        | Reseñas de productos                  |
| Shipping-Service     | 8090   | shipping_db      | Gestión de envíos                     |

---

## Despliegue Técnico

- **Instancia:** AWS EC2 t3.large (Ubuntu 24.04)
- **Arquitectura:** Microservicios
- **Framework:** Spring Boot
- **Base de datos:** MySQL 8.0
- **Orquestación:** Docker Compose
- **Cliente REST interno:** Spring Cloud OpenFeign
- **Manejo de errores:** `@RestControllerAdvice` + excepciones personalizadas
- **Logs:** SLF4J
- **Repositorio Maestro:** https://github.com/axlcortspov/Fullstack

### Comando de inicio

```bash
docker compose up -d --build
