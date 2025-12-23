# 👤 Manual de Usuario - SOCIES Backend

## Descripción
Este manual proporciona información detallada sobre cómo utilizar la API REST del sistema SOCIES (Sistema de Elecciones Electrónicas Seguras) para desarrolladores frontend y usuarios técnicos.

## Tabla de Contenidos
1. [Introducción al Sistema](#introducción-al-sistema)
2. [Autenticación y Autorización](#autenticación-y-autorización)
3. [Estructura de la API](#estructura-de-la-api)
4. [Endpoints de Autenticación](#endpoints-de-autenticación)
5. [Gestión de Usuarios](#gestión-de-usuarios)
6. [Gestión de Procesos Electorales](#gestión-de-procesos-electorales)
7. [Gestión de Candidatos](#gestión-de-candidatos)
8. [Sistema de Votación](#sistema-de-votación)
9. [Ejemplos de Uso](#ejemplos-de-uso)
10. [Códigos de Error](#códigos-de-error)
11. [Mejores Prácticas](#mejores-prácticas)

---

## 1. Introducción al Sistema

### ¿Qué es SOCIES?
SOCIES es un sistema backend robusto para elecciones electrónicas que garantiza:
- **Seguridad**: Autenticación JWT y encriptación de datos
- **Integridad**: Sistema de hashing tipo blockchain para votos
- **Transparencia**: Trazabilidad completa de todas las operaciones
- **Escalabilidad**: Arquitectura REST para múltiples frontends

### Características Principales
- API REST completa con documentación Swagger
- Autenticación basada en JWT (JSON Web Tokens)
- Sistema de roles y permisos
- Validación de integridad con hashing SHA-256
- Base de datos PostgreSQL para persistencia
- Logs detallados para auditoría

### URL Base
- **Desarrollo**: `http://localhost:8080`
- **Producción**: `https://api.socies.tu-dominio.com`

---

## 2. Autenticación y Autorización

### Sistema de JWT
El sistema utiliza JSON Web Tokens (JWT) para autenticación. Cada token tiene:
- **Duración**: 24 horas por defecto
- **Algoritmo**: HS256
- **Claims**: user_id, roles, email, exp

### Roles del Sistema
- **ADMINISTRADOR**: Acceso completo al sistema
- **SUPERVISOR**: Gestión de procesos electorales
- **VOTANTE**: Solo puede votar en procesos activos
- **OBSERVADOR**: Solo lectura de resultados públicos

### Headers Requeridos
```http
Authorization: Bearer <jwt-token>
Content-Type: application/json
Accept: application/json
```

---

## 3. Estructura de la API

### Prefijo de API
Todos los endpoints comienzan con `/api`

### Convenciones de URL
- **Recursos**: Nombres en plural (`/usuarios`, `/candidatos`)
- **Parámetros**: Identificadores numéricos (`/usuarios/123`)
- **Acciones**: Verbos HTTP estándar (GET, POST, PUT, DELETE, PATCH)

### Formato de Respuesta
```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": { ... },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

### Formato de Error
```json
{
  "success": false,
  "error": "CODIGO_ERROR",
  "message": "Descripción del error",
  "details": ["Campo específico con error"],
  "timestamp": "2024-01-01T12:00:00Z"
}
```

---

## 4. Endpoints de Autenticación

### POST /api/login
Autenticar usuario y obtener token JWT.

**Request:**
```json
{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 123,
      "email": "usuario@ejemplo.com",
      "nombre": "Juan Pérez",
      "rol": "ADMINISTRADOR"
    },
    "expiresIn": 86400
  }
}
```

**Errores Comunes:**
- `401`: Credenciales inválidas
- `400`: Datos de entrada inválidos
- `423`: Usuario bloqueado

### POST /api/registro
Registrar nuevo usuario (solo para administradores).

**Request:**
```json
{
  "nombre": "María García",
  "email": "maria@ejemplo.com",
  "password": "contraseñaSegura123",
  "rol": "SUPERVISOR",
  "cedula": "12345678"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Usuario creado exitosamente",
  "data": {
    "id": 124,
    "nombre": "María García",
    "email": "maria@ejemplo.com",
    "rol": "SUPERVISOR",
    "estado": "ACTIVO"
  }
}
```

### POST /api/refresh-token
Renovar token JWT antes de su expiración.

**Request:**
```json
{
  "refreshToken": "refresh_token_aqui"
}
```

---

## 5. Gestión de Usuarios

### GET /api/administrador/usuarios
Listar todos los usuarios (requiere rol ADMINISTRADOR).

**Query Parameters:**
- `page`: Número de página (default: 0)
- `size`: Elementos por página (default: 10)
- `sort`: Campo para ordenar (default: id)
- `direction`: Dirección de orden (ASC/DESC)
- `estado`: Filtrar por estado (ACTIVO/INACTIVO)
- `rol`: Filtrar por rol

**Response (200):**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "nombre": "Juan Pérez",
        "email": "juan@ejemplo.com",
        "rol": "ADMINISTRADOR",
        "estado": "ACTIVO",
        "fechaCreacion": "2024-01-01T10:00:00Z"
      }
    ],
    "totalElements": 50,
    "totalPages": 5,
    "currentPage": 0
  }
}
```

### GET /api/administrador/usuarios/{id}
Obtener usuario por ID.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@ejemplo.com",
    "cedula": "12345678",
    "rol": "ADMINISTRADOR",
    "estado": "ACTIVO",
    "fechaCreacion": "2024-01-01T10:00:00Z",
    "ultimoAcceso": "2024-01-02T15:30:00Z"
  }
}
```

### PUT /api/administrador/usuarios/{id}
Actualizar información de usuario.

**Request:**
```json
{
  "nombre": "Juan Carlos Pérez",
  "email": "juan.carlos@ejemplo.com",
  "rol": "SUPERVISOR"
}
```

### PATCH /api/administrador/usuarios/{id}/estado
Cambiar estado de usuario (activar/desactivar).

**Request:**
```json
{
  "estado": "INACTIVO",
  "motivo": "Usuario inactivo por solicitud"
}
```

### PATCH /api/administrador/usuarios/{id}/password
Cambiar contraseña de usuario.

**Request:**
```json
{
  "newPassword": "nuevaContraseñaSegura123",
  "confirmPassword": "nuevaContraseñaSegura123"
}
```

---

## 6. Gestión de Procesos Electorales

### GET /api/administrador/procesos-electorales
Listar procesos electorales.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "Elecciones Presidenciales 2024",
      "descripcion": "Proceso electoral para presidente",
      "fechaInicio": "2024-03-01T08:00:00Z",
      "fechaFin": "2024-03-01T18:00:00Z",
      "estado": "ACTIVO",
      "tipoVoto": "SECRETO"
    }
  ]
}
```

### POST /api/administrador/procesos-electorales
Crear nuevo proceso electoral.

**Request:**
```json
{
  "nombre": "Elecciones Municipales 2024",
  "descripcion": "Elección de alcalde y concejales",
  "fechaInicio": "2024-05-15T08:00:00Z",
  "fechaFin": "2024-05-15T18:00:00Z",
  "tipoVoto": "SECRETO",
  "metodoVoto": "ELECTRONICO"
}
```

### GET /api/administrador/cargos
Listar cargos disponibles.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "Presidente",
      "descripcion": "Presidente de la República",
      "activo": true
    },
    {
      "id": 2,
      "nombre": "Alcalde",
      "descripcion": "Alcalde Municipal",
      "activo": true
    }
  ]
}
```

---

## 7. Gestión de Candidatos

### GET /api/candidatos
Listar candidatos (público).

**Query Parameters:**
- `procesoId`: ID del proceso electoral
- `cargoId`: ID del cargo
- `partido`: Nombre del partido político

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "Ana Rodríguez",
      "partido": "Partido Democrático",
      "cargo": {
        "id": 1,
        "nombre": "Presidente"
      },
      "numeroLista": 101,
      "propuestas": "Propuestas de gobierno...",
      "foto": "https://example.com/photos/ana.jpg"
    }
  ]
}
```

### POST /api/administrador/candidatos
Registrar nuevo candidato.

**Request:**
```json
{
  "nombre": "Carlos Mendoza",
  "cedula": "87654321",
  "partido": "Partido Popular",
  "cargoId": 1,
  "procesoElectoralId": 1,
  "numeroLista": 102,
  "propuestas": "Plan de gobierno detallado..."
}
```

---

## 8. Sistema de Votación

### POST /api/votos
Registrar voto (requiere autenticación de votante).

**Request:**
```json
{
  "procesoElectoralId": 1,
  "candidaturaId": 1,
  "ubicacionVoto": {
    "departamento": "La Paz",
    "municipio": "La Paz",
    "zona": "Centro"
  }
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Voto registrado exitosamente",
  "data": {
    "votoId": "abc123def456",
    "hash": "a1b2c3d4e5f6...",
    "timestamp": "2024-03-01T14:30:00Z",
    "verificacion": "VALIDO"
  }
}
```

### GET /api/votos/verificar/{hash}
Verificar integridad de voto por hash.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "hash": "a1b2c3d4e5f6...",
    "valido": true,
    "timestamp": "2024-03-01T14:30:00Z",
    "procesoElectoral": "Elecciones Presidenciales 2024"
  }
}
```

### GET /api/resultados/{procesoId}
Obtener resultados de proceso electoral (público después del cierre).

**Response (200):**
```json
{
  "success": true,
  "data": {
    "procesoElectoral": {
      "id": 1,
      "nombre": "Elecciones Presidenciales 2024",
      "estado": "FINALIZADO"
    },
    "resultados": [
      {
        "candidato": "Ana Rodríguez",
        "partido": "Partido Democrático",
        "votos": 1250,
        "porcentaje": 45.5
      },
      {
        "candidato": "Carlos Mendoza",
        "partido": "Partido Popular",
        "votos": 1100,
        "porcentaje": 40.0
      }
    ],
    "totalVotos": 2750,
    "participacion": 75.5
  }
}
```

---

## 9. Ejemplos de Uso

### Ejemplo 1: Flujo Completo de Autenticación
```bash
# 1. Login
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@socies.com",
    "password": "admin123"
  }'

# Respuesta:
# {
#   "success": true,
#   "data": {
#     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#     "user": {...}
#   }
# }

# 2. Usar token en siguientes peticiones
curl -X GET http://localhost:8080/api/administrador/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

### Ejemplo 2: Crear Proceso Electoral Completo
```bash
# 1. Crear proceso electoral
curl -X POST http://localhost:8080/api/administrador/procesos-electorales \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Elecciones Estudiantiles 2024",
    "descripcion": "Elección de representante estudiantil",
    "fechaInicio": "2024-06-01T09:00:00Z",
    "fechaFin": "2024-06-01T17:00:00Z",
    "tipoVoto": "SECRETO"
  }'

# 2. Registrar candidatos
curl -X POST http://localhost:8080/api/administrador/candidatos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María Estudiante",
    "cedula": "11111111",
    "partido": "Lista Verde",
    "cargoId": 3,
    "procesoElectoralId": 2,
    "numeroLista": 201
  }'
```

### Ejemplo 3: Emitir Voto
```bash
# Usuario votante emite su voto
curl -X POST http://localhost:8080/api/votos \
  -H "Authorization: Bearer <voter-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "procesoElectoralId": 2,
    "candidaturaId": 5,
    "ubicacionVoto": {
      "departamento": "Cochabamba",
      "municipio": "Cochabamba",
      "zona": "Norte"
    }
  }'
```

---

## 10. Códigos de Error

### Errores de Autenticación (40x)
- **400 BAD_REQUEST**: Datos de entrada inválidos
- **401 UNAUTHORIZED**: Token inválido o expirado
- **403 FORBIDDEN**: Sin permisos para la operación
- **404 NOT_FOUND**: Recurso no encontrado
- **409 CONFLICT**: Conflicto (ej: usuario ya votó)
- **423 LOCKED**: Usuario bloqueado

### Errores de Servidor (50x)
- **500 INTERNAL_SERVER_ERROR**: Error interno del servidor
- **503 SERVICE_UNAVAILABLE**: Servicio temporalmente no disponible

### Códigos de Error Personalizados
- **USER_ALREADY_VOTED**: El usuario ya emitió su voto
- **ELECTION_NOT_ACTIVE**: El proceso electoral no está activo
- **INVALID_CANDIDATE**: Candidato no válido para este proceso
- **VOTE_VERIFICATION_FAILED**: Fallo en la verificación del voto

---

## 11. Mejores Prácticas

### Seguridad
- **Siempre usar HTTPS** en producción
- **Renovar tokens** antes de que expiren
- **Validar entrada** en el frontend antes de enviar
- **No almacenar contraseñas** en texto plano

### Performance
- **Usar paginación** para listas grandes
- **Implementar caché** para datos que no cambian frecuentemente
- **Comprimir respuestas** con gzip
- **Minimizar payloads** enviando solo datos necesarios

### Desarrollo
- **Documentar APIs** con Swagger/OpenAPI
- **Usar versionado** para cambios breaking
- **Implementar logs** detallados para debugging
- **Probar endpoints** con herramientas como Postman

### Manejo de Errores
```javascript
// Ejemplo en JavaScript/TypeScript
try {
  const response = await fetch('/api/votos', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(votoData)
  });
  
  const result = await response.json();
  
  if (!result.success) {
    switch(result.error) {
      case 'USER_ALREADY_VOTED':
        alert('Ya has emitido tu voto');
        break;
      case 'ELECTION_NOT_ACTIVE':
        alert('La elección no está activa');
        break;
      default:
        alert('Error: ' + result.message);
    }
  }
} catch (error) {
  console.error('Error de red:', error);
  alert('Error de conexión con el servidor');
}
```

---

## Swagger/OpenAPI

### Acceso a Documentación Interactiva
- **URL**: `http://localhost:8080/swagger-ui.html`
- **JSON**: `http://localhost:8080/v3/api-docs`

La documentación Swagger proporciona:
- Descripción detallada de todos los endpoints
- Ejemplos de request/response
- Posibilidad de probar APIs directamente
- Esquemas de datos

---

## Contacto y Soporte

Para preguntas técnicas sobre el uso de la API:
- **Documentación**: Ver archivos en `docs/`
- **Issues**: [GitHub Issues](https://github.com/socies-emi-uat/socies_elecciones_backend/issues)
- **Wiki**: [Proyecto Wiki](https://github.com/socies-emi-uat/socies_elecciones_backend/wiki)

---

**¡API Lista para Usar!** 🚀

Para configuración avanzada del sistema, consulta el [Manual de Administrador](manual-administrador.md).