
# Soporte TECH

Sistema web de gestión de soporte técnico desarrollado como proyecto
del curso Desarrollo de Aplicaciones Web y Patrones.

## Descripción

Soporte TECH es una aplicación web orientada a facilitar la gestión
de soporte tecnológico mediante usuarios, técnicos, solicitudes de
soporte, productos, clientes y operaciones de venta.

El sistema incorpora autenticación y autorización por roles,
administración de información, carrito de compras, ventas,
contacto con técnicos mediante correo electrónico e
internacionalización español/inglés.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Thymeleaf
- Bootstrap
- JavaScript
- MySQL
- Maven
- Spring Mail
- Git
- GitHub

## Requisitos

Para ejecutar el proyecto se requiere:

- Java JDK 21
- MySQL
- Maven
- NetBeans o IDE compatible
- Conexión a Internet para determinadas dependencias

## Base de datos

La aplicación utiliza la base de datos:

tiendatech

El script SQL debe ejecutarse antes de iniciar la aplicación.

La base de datos contiene las tablas:

- usuario
- categoria
- producto
- cliente
- tecnico
- solicitud_soporte
- venta
- detalle_venta

## Configuración

La configuración principal se encuentra en:

src/main/resources/application.properties

La aplicación utiliza el puerto:

55

## Ejecución

Ejecutar el proyecto mediante Spring Boot.

Una vez iniciado, acceder desde el navegador a:

http://localhost:55

## Usuarios de prueba

Administrador

Usuario: admin  
Contraseña: 123456  
Rol: ADMIN

Usuario estándar

Usuario: kendall  
Contraseña: 123456  
Rol: USER

## Roles

ADMIN:

Tiene acceso a los módulos administrativos de categorías,
clientes, productos y técnicos.

USER:

Puede consultar productos, utilizar el carrito, realizar
operaciones, consultar técnicos, contactar soporte y registrar
solicitudes.

## Módulos principales

- Inicio de sesión
- Registro de usuarios
- Autenticación
- Control de acceso por roles
- Categorías
- Clientes
- Productos
- Técnicos
- Solicitudes de soporte
- Problemas comunes
- Carrito de compras
- Ventas
- Contacto mediante correo electrónico
- Internacionalización español/inglés

## Arquitectura

El sistema utiliza el patrón MVC y separación por capas:

- Domain
- Repository
- Service
- Controller
- Templates

La persistencia se realiza mediante Spring Data JPA e Hibernate
sobre una base de datos MySQL.

## Integrantes

- Kendall Artavia Delgado
- Santiago Arce Garita
- Angel Mora Navarro

## Curso

Desarrollo de Aplicaciones Web y Patrones

Universidad Fidélitas

2026