# 📚 Documentación SOCIES - Sistema de Elecciones Electrónicas Seguras

## Descripción
Bienvenido a la documentación completa del sistema SOCIES (Sistema de Elecciones Electrónicas Seguras). Esta documentación está organizada en manuales específicos según tu rol y necesidades.

## 📋 Manuales Disponibles

### 1. 📦 [Manual de Instalación](manual-instalacion.md)
**Para**: Desarrolladores y administradores de sistema
**Contenido**:
- Requisitos del sistema
- Instalación paso a paso
- Configuración inicial
- Instalación con Docker
- Solución de problemas de instalación

### 2. 👤 [Manual de Usuario](manual-usuario.md)
**Para**: Desarrolladores frontend, integradores de API
**Contenido**:
- Guía de uso de la API REST
- Autenticación y autorización
- Endpoints disponibles
- Ejemplos de uso
- Códigos de error y mejores prácticas

### 3. ⚙️ [Manual de Administrador](manual-administrador.md)
**Para**: Administradores de sistema, DevOps, personal técnico
**Contenido**:
- Configuración avanzada
- Deployment en producción
- Monitoreo y logs
- Seguridad y backup
- Escalabilidad y mantenimiento

## 🚀 Inicio Rápido

### Para Desarrolladores
1. Lee el [Manual de Instalación](manual-instalacion.md) para configurar tu entorno
2. Consulta el [Manual de Usuario](manual-usuario.md) para usar la API

### Para Administradores
1. Sigue el [Manual de Instalación](manual-instalacion.md) para el setup inicial
2. Consulta el [Manual de Administrador](manual-administrador.md) para configuración de producción

### Para Usuarios Finales
El sistema SOCIES Backend es una API REST. Los usuarios finales interactúan con aplicaciones frontend que consumen esta API.

## 🏗️ Arquitectura del Sistema

```
Frontend (Web/Mobile) ←→ API REST (SOCIES Backend) ←→ PostgreSQL Database
                                    ↕
                              Blockchain Security
```

## 🔧 Stack Tecnológico

- **Backend**: Spring Boot 3.4+ (Java 17+)
- **Base de Datos**: PostgreSQL 14+
- **Seguridad**: JWT + Hashing SHA-256
- **Containerización**: Docker + Docker Compose
- **Build Tool**: Gradle
- **API Documentation**: Swagger/OpenAPI

## 📞 Soporte y Contacto

### Reportar Problemas
- **GitHub Issues**: [Crear issue](https://github.com/socies-emi-uat/socies_elecciones_backend/issues)
- **Wiki del Proyecto**: [Ver wiki](https://github.com/socies-emi-uat/socies_elecciones_backend/wiki)

### Contribuir
1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Crear Pull Request

### Equipo de Desarrollo
Este proyecto es desarrollado por la **Escuela Militar de Ingeniería - Unidad Académica del Trópico** como parte de un proyecto académico con propósito social.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](../LICENSE) para más detalles.

---

## 🗂️ Estructura de la Documentación

```
docs/
├── README.md                    # Este archivo (índice)
├── manual-instalacion.md       # Manual de instalación
├── manual-usuario.md           # Manual de usuario/API
└── manual-administrador.md     # Manual de administrador
```

## 🔄 Versiones de Documentación

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0.0   | 2024-01-01 | Documentación inicial completa |

---

**¡Selecciona el manual apropiado para tu rol y comienza a trabajar con SOCIES!** 🗳️
