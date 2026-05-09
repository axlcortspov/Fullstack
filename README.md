# Proyecto Semestral: ShopFlow

## Integrante
- Axel Cortés

## Estado del Sistema (Hito 1.5)

| Microservicio   | Puerto | DB Name     | Funcionalidad                          |
| :-------------- | :----- | :---------- | :------------------------------------- |
| Auth-Service    | 8081   | auth_db     | Registro y Login                       |
| User-Service    | 8082   | user_db     | CRUD de usuarios                       |
| Product-Service | 8083   | product_db  | CRUD de productos                      |
| Cart-Service    | 8084   | cart_db     | Gestión de carrito de compras          |
| Order-Service   | 8085   | order_db    | Gestión de órdenes y estado de compra  |

## Descripción del Sistema

**ShopFlow** es un sistema de comercio electrónico basado en arquitectura de microservicios.  
Permite gestionar usuarios, autenticación, productos, carrito de compras y órdenes de manera desacoplada, facilitando la escalabilidad y mantenimiento del sistema.

Cada microservicio opera de forma independiente, con su propia base de datos, siguiendo principios de diseño de sistemas distribuidos.

## Despliegue Técnico

- **Instancia:** AWS EC2 t3.large (Ubuntu 24.04)
- **Arquitectura:** Microservicios con Spring Boot + Docker
- **Base de Datos:** MySQL 8.0 (una base de datos por servicio)
- **Orquestación:** Docker Compose
- **Comando de inicio:** docker compose up -d --build
- **Repositorio Maestro:** https://github.com/axlcortspov/Fullstack
