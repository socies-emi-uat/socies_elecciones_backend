# Pruebas de Carga y Estrés - SOCIES

## Descripción

El sistema SOCIES ahora incluye funcionalidades completas para realizar pruebas de carga y estrés con seguimiento de resultados diarios. Esta funcionalidad permite evaluar el rendimiento del sistema bajo diferentes condiciones de carga.

## Características

### ✅ Pruebas de Carga
- Simulación de usuarios concurrentes
- Medición de tiempos de respuesta
- Cálculo de throughput (solicitudes por segundo)
- Registro de tasa de éxito/fallo

### ✅ Pruebas de Estrés
- Pruebas más agresivas con mayor carga
- Identificación de puntos de quiebre del sistema
- Medición de comportamiento bajo presión extrema

### ✅ Resultados por Día
- Almacenamiento automático por fecha
- Resúmenes diarios con métricas agregadas
- Comparación histórica de rendimiento
- Reportes por rango de fechas

## Endpoints Disponibles

### Ejecutar Pruebas

#### POST `/api/administrador/load-testing/load-test`
Ejecuta una prueba de carga.

**Request Body:**
```json
{
  "endpoint": "/api/administrador/usuarios",
  "concurrentUsers": 10,
  "totalRequests": 100,
  "durationSeconds": 30,
  "notes": "Prueba de carga en endpoint de usuarios"
}
```

#### POST `/api/administrador/load-testing/stress-test`
Ejecuta una prueba de estrés (automáticamente aumenta la carga).

**Request Body:** (mismo formato que load-test)

### Consultar Resultados

#### GET `/api/administrador/load-testing/results/daily?date=2024-01-15`
Obtiene todos los resultados para una fecha específica.

#### GET `/api/administrador/load-testing/results/daily/summary?date=2024-01-15&testType=LOAD`
Obtiene resumen diario para un tipo de prueba específico.

#### GET `/api/administrador/load-testing/results/range?startDate=2024-01-01&endDate=2024-01-31&testType=LOAD`
Obtiene resúmenes para un rango de fechas.

#### GET `/api/administrador/load-testing/endpoints/test`
Lista endpoints disponibles para testing.

## Configuración

### Variables de Entorno
```properties
# Habilitar pruebas automáticas programadas (opcional)
loadtest.scheduled.enabled=false

# Endpoints de monitoreo
management.endpoints.web.exposure.include=health,metrics,info
```

### Pruebas Programadas
El sistema puede ejecutar pruebas automáticas:
- **Diarias:** Todos los días a las 2:00 AM (si está habilitado)
- **Semanales:** Domingos a las 3:00 AM para pruebas de estrés

Para habilitar: `loadtest.scheduled.enabled=true`

## Métricas Registradas

Cada prueba registra las siguientes métricas:

| Métrica | Descripción |
|---------|-------------|
| `averageResponseTime` | Tiempo promedio de respuesta (ms) |
| `minResponseTime` | Tiempo mínimo de respuesta (ms) |
| `maxResponseTime` | Tiempo máximo de respuesta (ms) |
| `throughput` | Solicitudes exitosas por segundo |
| `successfulRequests` | Número de solicitudes exitosas |
| `failedRequests` | Número de solicitudes fallidas |
| `successRate` | Porcentaje de éxito |
| `concurrentUsers` | Usuarios concurrentes simulados |

## Ejemplos de Uso

### 1. Prueba de Carga Básica
```bash
curl -X POST http://localhost:8080/api/administrador/load-testing/load-test \
  -H "Content-Type: application/json" \
  -d '{
    "endpoint": "/api/administrador/usuarios",
    "concurrentUsers": 5,
    "totalRequests": 50,
    "durationSeconds": 30,
    "notes": "Prueba básica del endpoint de usuarios"
  }'
```

### 2. Consultar Resultados del Día
```bash
curl "http://localhost:8080/api/administrador/load-testing/results/daily?date=2024-01-15"
```

### 3. Resumen Semanal
```bash
curl "http://localhost:8080/api/administrador/load-testing/results/range?startDate=2024-01-08&endDate=2024-01-15&testType=LOAD"
```

## Interpretación de Resultados

### 🟢 Rendimiento Óptimo
- Tiempo de respuesta < 200ms
- Tasa de éxito > 95%
- Throughput estable

### 🟡 Rendimiento Aceptable
- Tiempo de respuesta 200-500ms
- Tasa de éxito 85-95%
- Throughput variable

### 🔴 Rendimiento Deficiente
- Tiempo de respuesta > 500ms
- Tasa de éxito < 85%
- Throughput muy bajo

## Endpoints Recomendados para Testing

El sistema incluye endpoints críticos preconfigurados:
- `/api/login` - Autenticación
- `/api/administrador/usuarios` - Gestión de usuarios
- `/api/administrador/proceso-electoral` - Procesos electorales
- `/api/votante/proceso-electoral` - Vista de votante
- `/api/votante/perfil` - Perfil de votante

## Consideraciones

1. **Entorno de Producción:** Ejecutar pruebas fuera de horarios críticos
2. **Base de Datos:** Las pruebas pueden impactar el rendimiento general
3. **Recursos:** Monitorear CPU y memoria durante las pruebas
4. **Redes:** Considerar la latencia de red en los resultados
5. **Seguridad:** Usar endpoints públicos o configurar autenticación apropiada

## Monitoreo Adicional

El sistema expone endpoints de Actuator para monitoreo:
- `/actuator/health` - Estado del sistema
- `/actuator/metrics` - Métricas detalladas
- `/actuator/info` - Información de la aplicación
