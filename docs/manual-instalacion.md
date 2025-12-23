# 📦 Manual de Instalación - SOCIES Backend

## Descripción
Este manual proporciona instrucciones detalladas para instalar y configurar el sistema backend de SOCIES (Sistema de Elecciones Electrónicas Seguras).

## Tabla de Contenidos
1. [Requisitos del Sistema](#requisitos-del-sistema)
2. [Instalación de Dependencias](#instalación-de-dependencias)
3. [Configuración de Base de Datos](#configuración-de-base-de-datos)
4. [Configuración del Proyecto](#configuración-del-proyecto)
5. [Instalación y Ejecución](#instalación-y-ejecución)
6. [Verificación de la Instalación](#verificación-de-la-instalación)
7. [Instalación con Docker](#instalación-con-docker)
8. [Solución de Problemas](#solución-de-problemas)

---

## 1. Requisitos del Sistema

### Requisitos Mínimos
- **Sistema Operativo**: Windows 10/11, macOS 10.15+, o Linux (Ubuntu 18.04+)
- **Memoria RAM**: 4 GB mínimo, 8 GB recomendado
- **Espacio en Disco**: 2 GB libres
- **Conexión a Internet**: Requerida para descargar dependencias

### Software Requerido
- **Java 17 o superior** (OpenJDK recomendado)
- **PostgreSQL 14 o superior**
- **Git** (para clonar el repositorio)
- **Gradle** (incluido como wrapper en el proyecto)

### Software Opcional
- **Docker** y **Docker Compose** (para instalación con contenedores)
- **Postman** o **Insomnia** (para pruebas de API)

---

## 2. Instalación de Dependencias

### Instalación de Java 17

#### En Windows:
1. Descargar OpenJDK 17 desde [Adoptium](https://adoptium.net/)
2. Ejecutar el instalador y seguir las instrucciones
3. Configurar la variable de entorno `JAVA_HOME`
4. Verificar la instalación:
```cmd
java -version
```

#### En macOS:
```bash
# Usando Homebrew
brew install openjdk@17

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/opt/homebrew/opt/openjdk@17' >> ~/.zshrc
source ~/.zshrc
```

#### En Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Verificar instalación
java -version
```

### Instalación de PostgreSQL

#### En Windows:
1. Descargar PostgreSQL desde [postgresql.org](https://www.postgresql.org/download/windows/)
2. Ejecutar el instalador
3. Recordar la contraseña del usuario `postgres`
4. Asegurar que el servicio esté ejecutándose

#### En macOS:
```bash
# Usando Homebrew
brew install postgresql@14
brew services start postgresql@14
```

#### En Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Instalación de Git
- **Windows**: Descargar desde [git-scm.com](https://git-scm.com/)
- **macOS**: `brew install git`
- **Linux**: `sudo apt install git`

---

## 3. Configuración de Base de Datos

### Crear Base de Datos
1. Conectarse a PostgreSQL como usuario postgres:
```bash
sudo -u postgres psql
```

2. Crear la base de datos:
```sql
CREATE DATABASE eleccionesDB;
CREATE USER socies_user WITH ENCRYPTED PASSWORD 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON DATABASE eleccionesDB TO socies_user;
\q
```

### Verificar Conexión
```bash
psql -h localhost -U socies_user -d eleccionesDB
```

---

## 4. Configuración del Proyecto

### Clonar el Repositorio
```bash
git clone https://github.com/socies-emi-uat/socies_elecciones_backend.git
cd socies_elecciones_backend
```

### Configurar Variables de Entorno
1. Copiar el archivo de ejemplo:
```bash
cp .env.example .env
```

2. Editar el archivo `.env` con tus configuraciones:
```env
# Base de datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=eleccionesDB
DB_USERNAME=socies_user
DB_PASSWORD=tu_contraseña_segura

# JWT
JWT_SECRET=tu_clave_secreta_jwt_muy_larga_y_segura

# Configuración del servidor
SERVER_PORT=8080
```

### Configurar application.properties
Editar `src/main/resources/application.properties`:
```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5432/eleccionesDB
spring.datasource.username=socies_user
spring.datasource.password=tu_contraseña_segura
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuración JWT
jwt.secret=${JWT_SECRET:tu_clave_secreta_jwt_muy_larga_y_segura}

# Configuración del servidor
server.port=${SERVER_PORT:8080}

# Configuración de logging
logging.level.com.socies=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

## 5. Instalación y Ejecución

### Usando Gradle (Recomendado)

#### En Linux/macOS:
```bash
# Dar permisos de ejecución al wrapper
chmod +x gradlew

# Compilar el proyecto
./gradlew build

# Ejecutar la aplicación
./gradlew bootRun
```

#### En Windows:
```cmd
# Compilar el proyecto
gradlew.bat build

# Ejecutar la aplicación
gradlew.bat bootRun
```

### Usando JAR Compilado
```bash
# Compilar
./gradlew build

# Ejecutar JAR
java -jar build/libs/activos-0.0.1-SNAPSHOT.jar
```

---

## 6. Verificación de la Instalación

### Verificar que el Servidor está Funcionando
1. Abrir navegador y navegar a: `http://localhost:8080`
2. Deberías ver una respuesta del servidor (puede ser un error 404, pero indica que está ejecutándose)

### Probar Endpoint de Salud
```bash
curl http://localhost:8080/actuator/health
```

### Verificar Swagger UI (si está habilitado)
Navegar a: `http://localhost:8080/swagger-ui.html`

### Verificar Base de Datos
```bash
# Conectarse a la base de datos y verificar tablas
psql -h localhost -U socies_user -d eleccionesDB -c "\dt"
```

---

## 7. Instalación con Docker

### Requisitos
- Docker 20.10 o superior
- Docker Compose 2.0 o superior

### Usando Docker Compose (Recomendado)
1. Crear archivo `docker-compose.yml`:
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: eleccionesDB
      POSTGRES_USER: socies_user
      POSTGRES_PASSWORD: tu_contraseña_segura
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  socies-backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=eleccionesDB
      - DB_USERNAME=socies_user
      - DB_PASSWORD=tu_contraseña_segura
    depends_on:
      - postgres

volumes:
  postgres_data:
```

2. Ejecutar:
```bash
docker-compose up -d
```

### Usando Docker Manual
```bash
# Crear red
docker network create socies-network

# Ejecutar PostgreSQL
docker run -d --name postgres-socies \
  --network socies-network \
  -e POSTGRES_DB=eleccionesDB \
  -e POSTGRES_USER=socies_user \
  -e POSTGRES_PASSWORD=tu_contraseña_segura \
  -p 5432:5432 \
  postgres:14

# Construir imagen de la aplicación
docker build -t socies-backend .

# Ejecutar aplicación
docker run -d --name socies-app \
  --network socies-network \
  -p 8080:8080 \
  -e DB_HOST=postgres-socies \
  socies-backend
```

---

## 8. Solución de Problemas

### Error: "java: command not found"
**Solución**: Verificar que Java esté instalado y configurado en el PATH
```bash
echo $JAVA_HOME
which java
```

### Error de Conexión a Base de Datos
**Causas Comunes**:
- PostgreSQL no está ejecutándose
- Credenciales incorrectas
- Firewall bloqueando conexión

**Soluciones**:
```bash
# Verificar que PostgreSQL esté ejecutándose
sudo systemctl status postgresql

# Verificar conexión manual
psql -h localhost -U socies_user -d eleccionesDB

# Verificar configuración en application.properties
```

### Error: "Port 8080 already in use"
**Solución**: Cambiar puerto en `application.properties` o terminar proceso:
```bash
# Encontrar proceso usando puerto 8080
lsof -i :8080

# Terminar proceso
kill -9 <PID>
```

### Error de Memoria Insuficiente
**Solución**: Aumentar memoria para Gradle:
```bash
export GRADLE_OPTS="-Xmx2g -XX:MaxMetaspaceSize=512m"
```

### Problemas con Gradle Wrapper
**Solución**: Re-generar wrapper:
```bash
gradle wrapper --gradle-version 8.13
```

### Error de Dependencias
**Solución**: Limpiar caché de Gradle:
```bash
./gradlew clean build --refresh-dependencies
```

---

## Contacto y Soporte

Para soporte técnico adicional:
- **Repositorio**: [GitHub](https://github.com/socies-emi-uat/socies_elecciones_backend)
- **Issues**: Crear un issue en GitHub para reportar problemas
- **Documentación**: Ver `docs/` para manuales adicionales

---

**¡Instalación Completada!** 🎉

Una vez completada la instalación, consulta el [Manual de Usuario](manual-usuario.md) para aprender a usar el sistema y el [Manual de Administrador](manual-administrador.md) para configuraciones avanzadas.