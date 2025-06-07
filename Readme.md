# 🗳️ SOCIES - Sistema de Elecciones Electrónicas Seguras (Backend)

## 1. 📄 Descripción del proyecto

Este proyecto es el **backend oficial del sistema web SOCIES**, una plataforma electoral digital respaldada mediante tecnología **blockchain**. Se encarga de administrar y asegurar los procesos clave como autenticación, gestión de usuarios, registro de candidaturas, votaciones y verificación de resultados.

El backend se ha desarrollado utilizando **Spring Boot + PostgreSQL**, exponiendo una **API REST segura**. Todos los votos emitidos están protegidos criptográficamente y respaldados con una **huella digital (hash) tipo blockchain**, lo que hace que el sistema sea **extremadamente difícil de corromper o manipular**.

### 🎯 Objetivo principal

- Garantizar procesos electorales confiables, seguros y auditables.
- Proporcionar integridad mediante hashing y validación de transacciones (tipo blockchain).
- Exponer servicios RESTful seguros para ser consumidos por una interfaz web frontend.

---

## 2. ⚙️ Configuración inicial

### ✅ Requisitos previos

- Java 17+
- Gradle (wrapper incluido)
- PostgreSQL 14+
- Docker (opcional)
- Postman o Swagger (para pruebas de API)
- Git

### 📥 Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/socies_elecciones_backend.git
cd socies_elecciones_backend
```

---

## 3. 🚀 Cómo ejecutar el proyecto

### ▶️ Compilar y correr con Gradle

> Para Windows:
```bash
gradlew.bat bootRun
gradlew.bat Test (opcional para testeo)
gradlew.bat Clean (opcional para Limpieza de Cache)
```

### 🔧 Configuración de entorno (`application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eleccionesDB
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Clave secreta para JWT
jwt.secret=4a436dd4a40284253c8686459bf654c9a7d7781847d112b0b0e14ded33fa10dd
```

---

## 4. 📡 Endpoints clave (API REST)

> Todos bajo el prefijo `/api`

### 🔐 Autenticación
- `POST /api/login` – Iniciar sesión
- `POST /api/registro` – Registrar nuevo usuario con rol

### 👤 Usuarios (`/api/administrador/usuarios`)
- `GET /` – Lista todos los usuarios
- `GET /{id}` – Ver usuario por ID
- `POST /` – Crear nuevo usuario
- `PUT /{id}` – Actualizar información del usuario
- `PATCH /{id}/estado` – Activar o desactivar usuario
- `PATCH /{id}/password` – Cambiar contraseña

### 📊 Estado de procesos (`/api/administrador/estado-proceso`)
- `GET /` – Lista de estados
- `POST /` – Crear nuevo estado

### 🏛️ Cargos (`/api/administrador/cargos`)
- `GET /` – Lista de cargos
- `POST /` – Crear nuevo cargo

### 🔐 Blockchain (próximamente)
- `POST /votos` – Registro de voto con generación de hash tipo blockchain
- `GET /votos/usuario/{id}`
- `GET /votos/proceso/{id}`

> Cada voto será convertido a una **transacción digital con SHA-256**, y respaldado en una cadena de bloques local (blockchain-like) para preservar su integridad.

---

## 5. ✨ Otras mejoras

### 🏷️ Badges

![Java](https://img.shields.io/badge/Java-17-blue)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.0-success)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)
![Security](https://img.shields.io/badge/Security-Blockchain-green)
![License](https://img.shields.io/badge/license-MIT-green)

### 📚 Documentación adicional

- [📦 Colección Postman (en construcción)](https://www.postman.com/)
- [📘 Wiki del Proyecto](https://github.com/tu-usuario/socies_elecciones_backend/wiki)
- [🧪 Swagger UI](http://localhost:8080/swagger-ui.html) *(si está activado)*

---

## 👥 Equipo de desarrollo

Este sistema está siendo construido por un equipo de "poner numero" desarrolladores como parte de un proyecto académico de la "Escuela Militar de Ingenieria - Unidad Academica del Tropico" con propósito social, donde la **transparencia y resistencia a la manipulación de elecciones** es prioridad.
