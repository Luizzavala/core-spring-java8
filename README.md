# Core Backend para Java 8

Este proyecto es un **core backend** que proporciona lo esencial para comenzar el desarrollo de una nueva aplicación en Java 8. El objetivo es ofrecer una estructura inicial que facilite la creación de servicios backend con Spring Boot, JPA, y otras herramientas útiles.

## Tecnologías

Este proyecto utiliza las siguientes tecnologías y herramientas:

- **Java 8**: Versión mínima requerida.
- **Spring Boot 2.7.15**: Framework para construir aplicaciones backend de manera rápida y eficiente.
- **MySQL**: Base de datos relacional.
- **JPA (Java Persistence API)**: Para el manejo de persistencia y acceso a datos.
- **Lombok**: Para reducir el código repetitivo de getters, setters, y constructores.
- **Maven**: Sistema de gestión de dependencias y construcción de proyectos.

## Requisitos previos

- **Java 8** o superior instalado.
- **Maven** instalado.
- **MySQL** configurado y en ejecución.

## Configuración del proyecto

1. Clona el repositorio:

    ```bash
    git clone https://github.com/Luizzavala/core-backend-java8.git
    ```

2. Ingresa al directorio del proyecto:

    ```bash
    cd core-backend-java8
    ```

3. Configura las propiedades de conexión a la base de datos en el archivo `application.properties` o `application.yml`:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    ```

4. Ejecuta el proyecto con Maven:

    ```bash
    mvn spring-boot:run
    ```

## Estructura del Proyecto

El proyecto sigue la estructura estándar de Spring Boot:

