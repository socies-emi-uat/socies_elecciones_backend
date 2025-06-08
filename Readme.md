<h1 align="center">🗳️ SOCIES - Sistema de Elecciones Electrónicas Seguras (Backend)</h1>

<p align="center">
  <b>Un backend robusto, seguro y respaldado por blockchain para procesos electorales digitales.</b><br>
  Desarrollado en Spring Boot + PostgreSQL + JWT + Hashing para una integridad total.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/SpringBoot-3.0-success?style=flat-square" />
  <img src="https://img.shields.io/badge/PostgreSQL-14-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Security-Blockchain-green?style=flat-square" />
  <img src="https://img.shields.io/badge/license-MIT-green?style=flat-square" />
</p>

---

## 1.📄 Descripción del proyecto

SOCIES es el sistema de backend para una plataforma de elecciones electrónicas con enfoque en **seguridad, transparencia e integridad**. Implementa autenticación JWT, hashing tipo blockchain y control de usuarios, candidatos y votos.

🛡️ **Resistencia a la corrupción**: Cada voto se convierte en una transacción firmada digitalmente (SHA-256).
🔗 **Blockchain-local**: Se almacena una cadena de hashes para verificar la integridad de la votación.
🌐 **API REST**: Expuesta para consumo desde un frontend moderno.

---

## 2.⚙️ Configuración inicial

### ✅ Requisitos

- Java 17+
- Gradle (wrapper incluido)
- PostgreSQL 14+
- Docker (opcional)
- Git
- Postman o Swagger

### 📥 Clonar el proyecto

```bash
git clone https://github.com/socies-emi-uat/socies_elecciones_backend.git
cd socies_elecciones_backend  (rama develop en produccion)
```
### Configuración importante

1. Copie `.env.example` a `.env`.
2. Rellene las variables con los valores reales.
---

## 🚀 Ejecución

### ▶️ Levantar con Gradle

```bash
./gradlew bootRun
# o en Windows
gradlew.bat bootRun
```

### 3. 🔧 Configuración

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eleccionesDB
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Secret
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

## 👥 Equipo
Este sistema está siendo construido por un equipo de desarrolladores como parte de un proyecto académico de la "Escuela Militar de Ingenieria - Unidad Academica del Tropico" con propósito social, donde la **transparencia y resistencia a la manipulación de elecciones** es prioridad.

<p align="center"><i>Contribuye, mejora y apoya la democracia digital 🇧🇴</i></p>
