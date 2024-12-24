# Explorador Literario

![badge literalura](https://github.com/user-attachments/assets/7adf1d10-2d6d-4cc3-92e2-b767fadbe75a)

## Índice

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Capturas](#capturas)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Requisitos](#requisitos)
5. [Configuración de Variables de Entorno](#configuración-de-variables-de-entorno)
    - [Variables necesarias](#variables-necesarias)
    - [Ejemplo de configuración](#ejemplo-de-configuración)
6. [Crear la base de datos](#crear-la-base-de-datos)

## Descripción del Proyecto

Explorador Literario es una aplicación de consola diseñada para interactuar con una base de datos de libros y autores. La aplicación obtiene información sobre libros y autores desde la API Gutendex, permitiendo almacenar y gestionar libros y autores en la base de datos.

El menú principal, visible en las capturas, permite realizar diversas operaciones como registrar, buscar y listar libros y autores, entre otras funciones.

### Capturas

<div>
  <img src="https://github.com/user-attachments/assets/725978f2-12ad-4e9a-a0b3-b8b784477676" alt="Menu principal" style="height: 300px;">
  <img src="https://github.com/user-attachments/assets/bc26c366-4d30-40ab-8a38-2ca2f07e0d0d" alt="Top 10 libros descargados" style="height: 300px;">
</div>

## Tecnologías Utilizadas

- **Spring Boot**: Para crear la aplicación de consola.
- **PostgreSQL**: Base de datos para almacenar libros y autores.
- **JPA**: Para interactuar con la base de datos.
- **Jackson**: Para la conversión de datos de la API.
- **Maven**: Gestor de dependencias.
- **API Gutendex**: Para obtener información sobre libros y autores.

## Requisitos

- **Java 17** o superior.
- **PostgreSQL** como sistema de base de datos.
- **Conexión a Internet** para acceder a la API Gutendex.

## Configuración de Variables de Entorno

Para que la aplicación funcione correctamente, es necesario configurar las siguientes variables de entorno en tu sistema operativo. Estas variables se utilizan para establecer la conexión con la base de datos PostgreSQL y definir ciertos parámetros importantes para el funcionamiento de la aplicación.

### Variables necesarias

- **`DB_HOST`**: Dirección o nombre del host donde se encuentra la base de datos PostgreSQL (por ejemplo, `localhost` o una dirección IP).
- **`DB_USER`**: Nombre de usuario que tiene permisos para acceder a la base de datos.
- **`DB_PASSWORD`**: Contraseña asociada al usuario de la base de datos.

### Ejemplo de configuración

Debes agregar las siguientes variables de entorno a tu sistema, reemplazando los valores según tu configuración:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}/explorador_literario
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.HSQLDialect
spring.jpa.hibernate.ddl-auto=update

```

## Crear la base de datos

La aplicación espera que exista una base de datos llamada **`explorador_literario`**. Debes crear esta base de datos en tu instancia de PostgreSQL antes de ejecutar la aplicación.

1. Accede a PostgreSQL usando tu cliente preferido, como la terminal o una herramienta gráfica (por ejemplo, pgAdmin).

2. Ejecuta el siguiente comando SQL para crear la base de datos:

```sql
  CREATE DATABASE explorador_literario;
```
