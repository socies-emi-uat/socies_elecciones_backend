# ⚙️ Manual de Administrador - SOCIES Backend

## Descripción
Este manual está dirigido a administradores del sistema y personal técnico responsable de la configuración, deployment, mantenimiento y monitoreo del sistema SOCIES (Sistema de Elecciones Electrónicas Seguras).

## Tabla de Contenidos
1. [Arquitectura del Sistema](#arquitectura-del-sistema)
2. [Configuración Avanzada](#configuración-avanzada)
3. [Deployment y Producción](#deployment-y-producción)
4. [Gestión de Base de Datos](#gestión-de-base-de-datos)
5. [Seguridad y Backup](#seguridad-y-backup)
6. [Monitoreo y Logs](#monitoreo-y-logs)
7. [Mantenimiento](#mantenimiento)
8. [Escalabilidad](#escalabilidad)
9. [Resolución de Problemas](#resolución-de-problemas)
10. [Procedimientos de Emergencia](#procedimientos-de-emergencia)

---

## 1. Arquitectura del Sistema

### Componentes Principales

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Load Balancer │
│   (React/Vue)   │◄──►│   (Nginx)       │◄──►│   (HAProxy)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   SOCIES API    │
                       │  (Spring Boot)  │
                       └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐    ┌─────────────────┐
                       │   PostgreSQL    │    │   Redis Cache   │
                       │   (Primary)     │    │   (Sessions)    │
                       └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   PostgreSQL    │
                       │   (Replica)     │
                       └─────────────────┘
```

### Stack Tecnológico
- **Backend**: Spring Boot 3.4+, Java 17+
- **Base de Datos**: PostgreSQL 14+
- **Cache**: Redis (opcional)
- **Web Server**: Nginx
- **Load Balancer**: HAProxy
- **Containerización**: Docker + Docker Compose
- **Orquestación**: Kubernetes (opcional)

### Puertos Utilizados
- **8080**: Aplicación Spring Boot
- **5432**: PostgreSQL
- **6379**: Redis
- **80/443**: Nginx (HTTP/HTTPS)
- **8404**: HAProxy Stats

---

## 2. Configuración Avanzada

### Variables de Entorno de Producción

#### Archivo `.env` completo:
```env
# === CONFIGURACIÓN DE BASE DE DATOS ===
DB_HOST=postgres-primary
DB_PORT=5432
DB_NAME=eleccionesDB
DB_USERNAME=socies_user
DB_PASSWORD=contraseña_muy_segura_123!
DB_POOL_SIZE=20
DB_TIMEOUT=30000

# Base de datos de replica (solo lectura)
DB_REPLICA_HOST=postgres-replica
DB_REPLICA_PORT=5432

# === CONFIGURACIÓN JWT ===
JWT_SECRET=clave_jwt_super_secreta_de_64_caracteres_minimo_para_produccion
JWT_EXPIRATION=86400
JWT_REFRESH_EXPIRATION=604800

# === CONFIGURACIÓN DEL SERVIDOR ===
SERVER_PORT=8080
SERVER_MAX_THREADS=200
SERVER_MIN_THREADS=10

# === CONFIGURACIÓN DE LOGS ===
LOG_LEVEL=INFO
LOG_FILE_PATH=/var/log/socies/application.log
LOG_MAX_FILE_SIZE=100MB
LOG_MAX_FILES=10

# === CONFIGURACIÓN DE SEGURIDAD ===
ENCRYPTION_KEY=clave_encriptacion_aes_256_bits
CORS_ALLOWED_ORIGINS=https://socies.tu-dominio.com,https://admin.socies.tu-dominio.com
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,PATCH,OPTIONS

# === CONFIGURACIÓN DE EMAIL ===
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=noreply@socies.com
SMTP_PASSWORD=contraseña_email
SMTP_STARTTLS=true

# === CONFIGURACIÓN DE STORAGE ===
AWS_ACCESS_KEY_ID=tu_access_key
AWS_SECRET_ACCESS_KEY=tu_secret_key
AWS_REGION=us-east-1
AWS_S3_BUCKET=socies-documents

# === CONFIGURACIÓN DE MONITOREO ===
METRICS_ENABLED=true
HEALTH_CHECK_ENABLED=true
PROMETHEUS_ENDPOINT=/metrics

# === CONFIGURACIÓN DE CACHE ===
REDIS_HOST=redis-cache
REDIS_PORT=6379
REDIS_PASSWORD=redis_password_123
REDIS_TIMEOUT=5000
CACHE_TTL=3600

# === CONFIGURACIÓN DE BLOCKCHAIN ===
BLOCKCHAIN_ENABLED=true
HASH_ALGORITHM=SHA-256
DIFFICULTY_LEVEL=4
```

### Configuración application-prod.properties

```properties
# === CONFIGURACIÓN DE PERFIL DE PRODUCCIÓN ===
spring.profiles.active=prod

# === BASE DE DATOS PRINCIPAL ===
spring.datasource.primary.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.primary.username=${DB_USERNAME}
spring.datasource.primary.password=${DB_PASSWORD}
spring.datasource.primary.driver-class-name=org.postgresql.Driver

# === BASE DE DATOS REPLICA ===
spring.datasource.replica.url=jdbc:postgresql://${DB_REPLICA_HOST}:${DB_REPLICA_PORT}/${DB_NAME}
spring.datasource.replica.username=${DB_USERNAME}
spring.datasource.replica.password=${DB_PASSWORD}
spring.datasource.replica.driver-class-name=org.postgresql.Driver

# === CONFIGURACIÓN DE POOL DE CONEXIONES ===
spring.datasource.primary.hikari.maximum-pool-size=${DB_POOL_SIZE}
spring.datasource.primary.hikari.minimum-idle=5
spring.datasource.primary.hikari.connection-timeout=${DB_TIMEOUT}
spring.datasource.primary.hikari.idle-timeout=600000
spring.datasource.primary.hikari.max-lifetime=1800000

# === JPA/HIBERNATE ===
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.show_sql=false
spring.jpa.properties.hibernate.use_sql_comments=false

# === CONFIGURACIÓN DEL SERVIDOR ===
server.port=${SERVER_PORT}
server.tomcat.max-threads=${SERVER_MAX_THREADS}
server.tomcat.min-spare-threads=${SERVER_MIN_THREADS}
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain

# === CONFIGURACIÓN SSL ===
server.ssl.enabled=true
server.ssl.key-store=/etc/ssl/socies/keystore.p12
server.ssl.key-store-password=keystore_password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=socies

# === SEGURIDAD ===
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
security.cors.allowed-origins=${CORS_ALLOWED_ORIGINS}
security.cors.allowed-methods=${CORS_ALLOWED_METHODS}

# === LOGGING ===
logging.level.com.socies=${LOG_LEVEL}
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
logging.file.name=${LOG_FILE_PATH}
logging.file.max-size=${LOG_MAX_FILE_SIZE}
logging.file.max-history=${LOG_MAX_FILES}

# === ACTUATOR ===
management.endpoints.web.exposure.include=health,metrics,prometheus,info
management.endpoint.health.show-details=when_authorized
management.metrics.export.prometheus.enabled=${METRICS_ENABLED}

# === CACHE ===
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT}
spring.redis.password=${REDIS_PASSWORD}
spring.redis.timeout=${REDIS_TIMEOUT}
spring.cache.type=redis

# === EMAIL ===
spring.mail.host=${SMTP_HOST}
spring.mail.port=${SMTP_PORT}
spring.mail.username=${SMTP_USERNAME}
spring.mail.password=${SMTP_PASSWORD}
spring.mail.properties.mail.smtp.starttls.enable=${SMTP_STARTTLS}
```

---

## 3. Deployment y Producción

### Docker Compose para Producción

```yaml
version: '3.8'

services:
  # === BASE DE DATOS PRINCIPAL ===
  postgres-primary:
    image: postgres:14
    environment:
      POSTGRES_DB: eleccionesDB
      POSTGRES_USER: socies_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_INITDB_ARGS: "--auth-host=md5"
    volumes:
      - postgres_primary_data:/var/lib/postgresql/data
      - ./scripts/init-primary.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - socies-network
    ports:
      - "5432:5432"
    command: postgres -c shared_preload_libraries=pg_stat_statements
    restart: unless-stopped

  # === BASE DE DATOS REPLICA ===
  postgres-replica:
    image: postgres:14
    environment:
      POSTGRES_DB: eleccionesDB
      POSTGRES_USER: socies_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      PGUSER: socies_user
    volumes:
      - postgres_replica_data:/var/lib/postgresql/data
    networks:
      - socies-network
    ports:
      - "5433:5432"
    depends_on:
      - postgres-primary
    restart: unless-stopped

  # === CACHE REDIS ===
  redis-cache:
    image: redis:7-alpine
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis_data:/data
    networks:
      - socies-network
    ports:
      - "6379:6379"
    restart: unless-stopped

  # === APLICACIÓN PRINCIPAL ===
  socies-backend:
    build:
      context: .
      dockerfile: Dockerfile.prod
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=postgres-primary
      - DB_REPLICA_HOST=postgres-replica
      - REDIS_HOST=redis-cache
    env_file:
      - .env
    volumes:
      - app_logs:/var/log/socies
      - ./ssl:/etc/ssl/socies:ro
    networks:
      - socies-network
    ports:
      - "8080:8080"
    depends_on:
      - postgres-primary
      - redis-cache
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  # === NGINX REVERSE PROXY ===
  nginx:
    image: nginx:alpine
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./ssl:/etc/ssl/socies:ro
      - nginx_logs:/var/log/nginx
    networks:
      - socies-network
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - socies-backend
    restart: unless-stopped

  # === MONITOREO ===
  prometheus:
    image: prom/prometheus
    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml:ro
      - prometheus_data:/prometheus
    networks:
      - socies-network
    ports:
      - "9090:9090"
    restart: unless-stopped

  grafana:
    image: grafana/grafana
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin_password_123
    volumes:
      - grafana_data:/var/lib/grafana
      - ./monitoring/grafana/dashboards:/etc/grafana/provisioning/dashboards:ro
    networks:
      - socies-network
    ports:
      - "3000:3000"
    restart: unless-stopped

volumes:
  postgres_primary_data:
  postgres_replica_data:
  redis_data:
  app_logs:
  nginx_logs:
  prometheus_data:
  grafana_data:

networks:
  socies-network:
    driver: bridge
```

### Dockerfile de Producción

```dockerfile
# === ETAPA 1: BUILD ===
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build de la aplicación
RUN chmod +x gradlew
RUN ./gradlew build -x test

# === ETAPA 2: RUNTIME ===
FROM openjdk:17-jre-slim

# Instalar herramientas necesarias
RUN apt-get update && apt-get install -y \
    curl \
    htop \
    && rm -rf /var/lib/apt/lists/*

# Crear usuario no-root
RUN groupadd -r socies && useradd -r -g socies socies

# Crear directorios
RUN mkdir -p /app /var/log/socies
RUN chown -R socies:socies /app /var/log/socies

# Copiar JAR de la etapa de build
COPY --from=builder /app/build/libs/*.jar /app/socies-backend.jar
RUN chown socies:socies /app/socies-backend.jar

# Cambiar a usuario no-root
USER socies

# Configuración JVM
ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

WORKDIR /app
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio
CMD java $JAVA_OPTS -jar socies-backend.jar
```

### Script de Deployment

```bash
#!/bin/bash
# deploy.sh - Script de deployment para producción

set -e

# Configuración
APP_NAME="socies-backend"
BACKUP_DIR="/backup/socies"
LOG_FILE="/var/log/deployment.log"

# Funciones
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a $LOG_FILE
}

backup_database() {
    log "Iniciando backup de base de datos..."
    pg_dump -h postgres-primary -U socies_user eleccionesDB > "$BACKUP_DIR/db_backup_$(date +%Y%m%d_%H%M%S).sql"
    log "Backup completado"
}

deployment_steps() {
    log "=== INICIANDO DEPLOYMENT ==="
    
    # 1. Backup
    backup_database
    
    # 2. Pull latest code
    log "Actualizando código..."
    git pull origin main
    
    # 3. Build nueva imagen
    log "Construyendo nueva imagen..."
    docker-compose -f docker-compose.prod.yml build socies-backend
    
    # 4. Ejecutar migraciones
    log "Ejecutando migraciones..."
    docker-compose -f docker-compose.prod.yml run --rm socies-backend \
        java -jar socies-backend.jar --spring.profiles.active=prod \
        --spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
    
    # 5. Rolling update
    log "Iniciando rolling update..."
    docker-compose -f docker-compose.prod.yml up -d socies-backend
    
    # 6. Health check
    log "Verificando health check..."
    sleep 30
    for i in {1..10}; do
        if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            log "✅ Health check OK"
            break
        else
            log "⏳ Esperando health check... ($i/10)"
            sleep 10
        fi
    done
    
    # 7. Limpiar imágenes viejas
    log "Limpiando imágenes Docker viejas..."
    docker image prune -f
    
    log "=== DEPLOYMENT COMPLETADO ==="
}

# Ejecutar deployment
deployment_steps
```

---

## 4. Gestión de Base de Datos

### Scripts de Backup

```bash
#!/bin/bash
# backup-db.sh

BACKUP_DIR="/backup/socies"
RETENTION_DAYS=30
DB_HOST="postgres-primary"
DB_NAME="eleccionesDB"
DB_USER="socies_user"

# Crear directorio si no existe
mkdir -p $BACKUP_DIR

# Backup con timestamp
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/socies_backup_$TIMESTAMP.sql"

# Realizar backup
log "Iniciando backup de base de datos..."
pg_dump -h $DB_HOST -U $DB_USER -d $DB_NAME --verbose > $BACKUP_FILE

# Comprimir backup
gzip $BACKUP_FILE

# Eliminar backups antiguos
find $BACKUP_DIR -name "socies_backup_*.sql.gz" -mtime +$RETENTION_DAYS -delete

log "Backup completado: ${BACKUP_FILE}.gz"
```

### Script de Restauración

```bash
#!/bin/bash
# restore-db.sh

BACKUP_FILE=$1
DB_HOST="postgres-primary"
DB_NAME="eleccionesDB"
DB_USER="socies_user"

if [ -z "$BACKUP_FILE" ]; then
    echo "Uso: $0 <archivo_backup.sql.gz>"
    exit 1
fi

# Verificar que el archivo existe
if [ ! -f "$BACKUP_FILE" ]; then
    echo "Error: Archivo $BACKUP_FILE no encontrado"
    exit 1
fi

# Confirmación
read -p "¿Estás seguro de restaurar $BACKUP_FILE? Esto sobrescribirá la base de datos actual. (y/N): " confirm
if [ "$confirm" != "y" ]; then
    echo "Cancelado"
    exit 0
fi

# Detener aplicación
docker-compose -f docker-compose.prod.yml stop socies-backend

# Descomprimir y restaurar
zcat $BACKUP_FILE | psql -h $DB_HOST -U $DB_USER -d $DB_NAME

# Reiniciar aplicación
docker-compose -f docker-compose.prod.yml start socies-backend

echo "Restauración completada"
```

### Configuración de Replica

```sql
-- En el servidor principal (Master)
-- postgresql.conf
wal_level = replica
max_wal_senders = 3
max_replication_slots = 3
synchronous_commit = on

-- pg_hba.conf
host replication socies_user postgres-replica md5

-- Crear usuario de replicación
CREATE ROLE replication_user REPLICATION LOGIN ENCRYPTED PASSWORD 'replication_password';
```

### Monitoreo de Base de Datos

```sql
-- Queries útiles para monitoreo

-- 1. Verificar conexiones activas
SELECT count(*) as active_connections,
       state,
       application_name
FROM pg_stat_activity
WHERE state IS NOT NULL
GROUP BY state, application_name;

-- 2. Consultas más lentas
SELECT query,
       mean_time,
       calls,
       total_time,
       rows,
       100.0 * shared_blks_hit / nullif(shared_blks_hit + shared_blks_read, 0) AS hit_percent
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- 3. Tamaño de tablas
SELECT schemaname,
       tablename,
       pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- 4. Estado de replicación
SELECT client_addr,
       state,
       sent_lsn,
       write_lsn,
       flush_lsn,
       replay_lsn,
       sync_state
FROM pg_stat_replication;
```

---

## 5. Seguridad y Backup

### Configuración de SSL/TLS

```bash
# Generar certificados SSL
openssl req -x509 -newkey rsa:4096 -keyout socies.key -out socies.crt -days 365 -nodes

# Crear keystore para Java
openssl pkcs12 -export -in socies.crt -inkey socies.key -out keystore.p12 -name socies

# Configurar en Nginx
server {
    listen 443 ssl http2;
    server_name socies.tu-dominio.com;
    
    ssl_certificate /etc/ssl/socies/socies.crt;
    ssl_certificate_key /etc/ssl/socies/socies.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;
    ssl_prefer_server_ciphers off;
    
    location / {
        proxy_pass http://socies-backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Configuración de Firewall

```bash
# UFW (Ubuntu)
ufw default deny incoming
ufw default allow outgoing
ufw allow ssh
ufw allow 80/tcp
ufw allow 443/tcp
ufw allow from 10.0.0.0/8 to any port 5432
ufw enable

# iptables alternativo
iptables -A INPUT -p tcp --dport 22 -j ACCEPT
iptables -A INPUT -p tcp --dport 80 -j ACCEPT
iptables -A INPUT -p tcp --dport 443 -j ACCEPT
iptables -A INPUT -p tcp --dport 5432 -s 10.0.0.0/8 -j ACCEPT
iptables -A INPUT -j DROP
```

### Estrategia de Backup

```bash
#!/bin/bash
# backup-strategy.sh - Backup completo del sistema

BACKUP_BASE="/backup/socies"
DATE=$(date +%Y%m%d_%H%M%S)

# 1. Backup de base de datos
pg_dump -h postgres-primary -U socies_user eleccionesDB > "$BACKUP_BASE/db/db_$DATE.sql"

# 2. Backup de archivos de configuración
tar -czf "$BACKUP_BASE/config/config_$DATE.tar.gz" \
    /etc/socies/ \
    /docker/socies/ \
    /var/log/socies/

# 3. Backup de certificados SSL
tar -czf "$BACKUP_BASE/ssl/ssl_$DATE.tar.gz" /etc/ssl/socies/

# 4. Sincronizar con almacenamiento remoto (S3, etc.)
aws s3 sync $BACKUP_BASE s3://socies-backups/ --delete

# 5. Limpiar backups locales antiguos (mantener 7 días)
find $BACKUP_BASE -type f -mtime +7 -delete
```

---

## 6. Monitoreo y Logs

### Configuración de Prometheus

```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'socies-backend'
    static_configs:
      - targets: ['socies-backend:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 10s

  - job_name: 'postgres'
    static_configs:
      - targets: ['postgres-exporter:9187']

  - job_name: 'redis'
    static_configs:
      - targets: ['redis-exporter:9121']

  - job_name: 'nginx'
    static_configs:
      - targets: ['nginx-exporter:9113']
```

### Dashboard de Grafana

```json
{
  "dashboard": {
    "title": "SOCIES System Monitoring",
    "panels": [
      {
        "title": "Request Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(http_requests_total[5m])",
            "legendFormat": "{{method}} {{status}}"
          }
        ]
      },
      {
        "title": "Response Time",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m]))",
            "legendFormat": "95th percentile"
          }
        ]
      },
      {
        "title": "Database Connections",
        "type": "singlestat",
        "targets": [
          {
            "expr": "pg_stat_database_numbackends{datname=\"eleccionesDB\"}"
          }
        ]
      }
    ]
  }
}
```

### Configuración de Logs Centralizados

```yaml
# logstash.conf
input {
  file {
    path => "/var/log/socies/application.log"
    type => "socies-app"
    codec => json
  }
  
  file {
    path => "/var/log/nginx/access.log"
    type => "nginx-access"
  }
}

filter {
  if [type] == "socies-app" {
    json {
      source => "message"
    }
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "socies-logs-%{+YYYY.MM.dd}"
  }
}
```

### Scripts de Alertas

```bash
#!/bin/bash
# health-check.sh

API_URL="http://localhost:8080/actuator/health"
SLACK_WEBHOOK="https://hooks.slack.com/services/YOUR/SLACK/WEBHOOK"

# Check API health
if ! curl -f $API_URL > /dev/null 2>&1; then
    MESSAGE="🚨 SOCIES API is DOWN! $(date)"
    curl -X POST -H 'Content-type: application/json' \
         --data "{\"text\":\"$MESSAGE\"}" \
         $SLACK_WEBHOOK
fi

# Check database
if ! pg_isready -h postgres-primary -p 5432 > /dev/null 2>&1; then
    MESSAGE="🚨 SOCIES Database is DOWN! $(date)"
    curl -X POST -H 'Content-type: application/json' \
         --data "{\"text\":\"$MESSAGE\"}" \
         $SLACK_WEBHOOK
fi
```

---

## 7. Mantenimiento

### Tareas de Mantenimiento Regular

```bash
#!/bin/bash
# maintenance.sh - Tareas de mantenimiento diarias

# 1. Limpiar logs antiguos
find /var/log/socies -name "*.log" -mtime +30 -delete

# 2. Vacuum de base de datos
psql -h postgres-primary -U socies_user -d eleccionesDB -c "VACUUM ANALYZE;"

# 3. Limpiar cache de Redis
redis-cli -h redis-cache -a $REDIS_PASSWORD FLUSHDB

# 4. Rotar logs de Nginx
logrotate /etc/logrotate.d/nginx

# 5. Actualizar estadísticas de Postgres
psql -h postgres-primary -U socies_user -d eleccionesDB -c "ANALYZE;"

# 6. Verificar espacio en disco
df -h | grep -E '9[0-9]%|100%' && echo "⚠️ Espacio en disco bajo"

# 7. Verificar memoria
free -m | awk 'NR==2{printf "%.1f%%\n", $3*100/$2 }' | \
    awk '{if($1 > 90) print "⚠️ Memoria alta: " $1}'
```

### Actualización del Sistema

```bash
#!/bin/bash
# update-system.sh

# 1. Crear backup antes de actualizar
./backup-db.sh

# 2. Actualizar dependencias de sistema
apt update && apt upgrade -y

# 3. Actualizar Docker
docker system prune -f
docker-compose pull

# 4. Actualizar aplicación
git pull origin main
docker-compose -f docker-compose.prod.yml build
docker-compose -f docker-compose.prod.yml up -d

# 5. Verificar que todo funcione
sleep 60
curl -f http://localhost:8080/actuator/health || {
    echo "❌ Error en health check post-actualización"
    # Rollback automático
    docker-compose -f docker-compose.prod.yml down
    docker-compose -f docker-compose.prod.yml up -d
}
```

---

## 8. Escalabilidad

### Configuración de Load Balancer

```yaml
# haproxy.cfg
global
    daemon
    maxconn 4096

defaults
    mode http
    timeout connect 5000ms
    timeout client 50000ms
    timeout server 50000ms

frontend socies_frontend
    bind *:80
    bind *:443 ssl crt /etc/ssl/socies/socies.pem
    redirect scheme https if !{ ssl_fc }
    default_backend socies_backend

backend socies_backend
    balance roundrobin
    option httpchk GET /actuator/health
    server app1 socies-backend-1:8080 check
    server app2 socies-backend-2:8080 check
    server app3 socies-backend-3:8080 check

listen stats
    bind *:8404
    stats enable
    stats uri /stats
    stats refresh 30s
```

### Configuración Multi-instancia

```yaml
# docker-compose.scale.yml
version: '3.8'

services:
  socies-backend:
    image: socies-backend:latest
    deploy:
      replicas: 3
    environment:
      - INSTANCE_ID={{.Task.Slot}}
    networks:
      - socies-network

  nginx:
    image: nginx:alpine
    depends_on:
      - socies-backend
    volumes:
      - ./nginx/nginx-lb.conf:/etc/nginx/nginx.conf:ro
```

### Auto-scaling con Kubernetes

```yaml
# k8s-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: socies-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: socies-backend
  template:
    metadata:
      labels:
        app: socies-backend
    spec:
      containers:
      - name: socies-backend
        image: socies-backend:latest
        ports:
        - containerPort: 8080
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"

---
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: socies-backend-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: socies-backend
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

---

## 9. Resolución de Problemas

### Problemas Comunes y Soluciones

#### 1. Alta Latencia en Requests
```bash
# Diagnóstico
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/health

# Soluciones
# - Aumentar pool de conexiones DB
# - Optimizar queries SQL
# - Añadir caché Redis
# - Escalar horizontalmente
```

#### 2. Error de Conexión a Base de Datos
```bash
# Verificar estado
pg_isready -h postgres-primary -p 5432

# Verificar logs
docker logs postgres-primary

# Reiniciar conexiones
docker-compose restart postgres-primary
```

#### 3. Memoria Insuficiente
```bash
# Monitorear memoria
docker stats

# Aumentar límites de memoria
# En docker-compose.yml:
deploy:
  resources:
    limits:
      memory: 4G
```

#### 4. Disco Lleno
```bash
# Verificar uso
df -h

# Limpiar logs
find /var/log -name "*.log" -mtime +7 -delete

# Limpiar Docker
docker system prune -a
```

### Script de Diagnóstico

```bash
#!/bin/bash
# diagnostics.sh

echo "=== DIAGNÓSTICO DEL SISTEMA SOCIES ==="

# 1. Estado de servicios
echo "1. Estado de servicios Docker:"
docker-compose ps

# 2. Uso de recursos
echo -e "\n2. Uso de recursos:"
docker stats --no-stream

# 3. Logs recientes
echo -e "\n3. Logs recientes (últimas 50 líneas):"
docker-compose logs --tail=50 socies-backend

# 4. Estado de base de datos
echo -e "\n4. Estado de base de datos:"
pg_isready -h postgres-primary -p 5432 && echo "✅ DB OK" || echo "❌ DB ERROR"

# 5. Conectividad de red
echo -e "\n5. Conectividad:"
curl -s -o /dev/null -w "API Status: %{http_code}\n" http://localhost:8080/actuator/health

# 6. Espacio en disco
echo -e "\n6. Espacio en disco:"
df -h | grep -E '(Filesystem|/dev/)'

# 7. Procesos Java
echo -e "\n7. Procesos Java activos:"
jps -v 2>/dev/null || echo "JPS no disponible"
```

---

## 10. Procedimientos de Emergencia

### Plan de Recuperación ante Desastres

```bash
#!/bin/bash
# disaster-recovery.sh

BACKUP_LOCATION="s3://socies-backups"
RECOVERY_DIR="/recovery"

echo "=== INICIANDO RECUPERACIÓN DE DESASTRE ==="

# 1. Descargar último backup
aws s3 sync $BACKUP_LOCATION $RECOVERY_DIR

# 2. Encontrar backup más reciente
LATEST_DB_BACKUP=$(ls -t $RECOVERY_DIR/db/*.sql | head -1)
LATEST_CONFIG_BACKUP=$(ls -t $RECOVERY_DIR/config/*.tar.gz | head -1)

# 3. Restaurar configuración
tar -xzf $LATEST_CONFIG_BACKUP -C /

# 4. Restaurar base de datos
createdb eleccionesDB
psql -d eleccionesDB < $LATEST_DB_BACKUP

# 5. Reiniciar servicios
docker-compose -f docker-compose.prod.yml up -d

# 6. Verificar funcionamiento
sleep 60
curl -f http://localhost:8080/actuator/health || {
    echo "❌ Error en recuperación"
    exit 1
}

echo "✅ Recuperación completada exitosamente"
```

### Procedimiento de Rollback

```bash
#!/bin/bash
# rollback.sh

# Obtener versión anterior
PREVIOUS_IMAGE=$(docker images socies-backend --format "table {{.Tag}}" | sed -n '2p')

if [ -z "$PREVIOUS_IMAGE" ]; then
    echo "❌ No hay imagen anterior disponible"
    exit 1
fi

echo "🔄 Realizando rollback a versión: $PREVIOUS_IMAGE"

# 1. Detener servicios actuales
docker-compose -f docker-compose.prod.yml stop socies-backend

# 2. Cambiar a imagen anterior
sed -i "s/socies-backend:latest/socies-backend:$PREVIOUS_IMAGE/" docker-compose.prod.yml

# 3. Reiniciar con imagen anterior
docker-compose -f docker-compose.prod.yml up -d socies-backend

# 4. Verificar
sleep 30
curl -f http://localhost:8080/actuator/health && {
    echo "✅ Rollback exitoso"
} || {
    echo "❌ Rollback falló"
    exit 1
}
```

### Contactos de Emergencia

```bash
# emergency-contacts.sh
ONCALL_PHONE="+1234567890"
SLACK_EMERGENCY="#socies-emergencies"
EMAIL_ADMIN="admin@socies.com"

send_alert() {
    local MESSAGE="$1"
    local SEVERITY="$2"
    
    # SMS
    curl -X POST "https://api.twilio.com/2010-04-01/Accounts/$TWILIO_SID/Messages.json" \
         -u "$TWILIO_SID:$TWILIO_TOKEN" \
         -d "To=$ONCALL_PHONE" \
         -d "From=$TWILIO_FROM" \
         -d "Body=SOCIES ALERT [$SEVERITY]: $MESSAGE"
    
    # Slack
    curl -X POST -H 'Content-type: application/json' \
         --data "{\"text\":\"🚨 SOCIES [$SEVERITY]: $MESSAGE\"}" \
         $SLACK_WEBHOOK
    
    # Email
    echo "$MESSAGE" | mail -s "SOCIES Alert [$SEVERITY]" $EMAIL_ADMIN
}

# Uso: send_alert "Database connection failed" "CRITICAL"
```

---

## Documentación Adicional

### Enlaces Útiles
- **Spring Boot Docs**: https://docs.spring.io/spring-boot/docs/current/reference/html/
- **PostgreSQL Docs**: https://www.postgresql.org/docs/
- **Docker Compose**: https://docs.docker.com/compose/
- **Nginx**: https://nginx.org/en/docs/
- **Prometheus**: https://prometheus.io/docs/

### Archivos de Configuración de Ejemplo
Todos los archivos de configuración mencionados están disponibles en:
- `config/examples/` - Ejemplos de configuración
- `scripts/` - Scripts de administración
- `monitoring/` - Configuraciones de monitoreo

---

**¡Sistema Administrado Correctamente!** 🛡️

Para soporte adicional, consulta el [Manual de Usuario](manual-usuario.md) y el [Manual de Instalación](manual-instalacion.md).