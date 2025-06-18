# API Candidaturas - Administrador

## Crear una candidatura

**Método:** `POST`  
**Endpoint:** `{{host}}/api/administrador/candidaturas`  
**Estado:** ✅ OK

### Respuesta Exitosa

```json
{
  "success": true,
  "message": "Candidatura creada exitosamente.",
  "data": {
    "nombreCandidatura": "Candidatura 2025",
    "lema": "Por un futuro mejor",
    "candidato": {
      "id": 1,
      "nombreCandidato": "Juan Actual",
      "apPaterno": "Perez actual",
      "apMaterno": "García actual",
      "ciCandidato": "1234567890",
      "fechaNacimiento": "1990-01-01",
      "fotoUrl": "https://example.com/foto_actualizada.jpg",
      "correoCandidato": "",
      "propuesta": "Propuesta actualizada",
      "estadoCandidatoId": 1,
      "cargoId": 1
    },
    "partido": {
      "nombrePartido": "Partido Actualizado",
      "sigla": "PA",
      "lema": "Renovación y orden",
      "logoUrl": "http://nuevo.logo/url.png",
      "colorHex": "#ff6600",
      "pais": "Bolivia",
      "representanteLegal": "Ana Maria",
      "descripcion": "Cambio de imagen y estructura",
      "direccionSede": "Zona Sur #456",
      "paginaWeb": "https://partidoactualizado.bo",
      "telefonoContacto": "78541236",
      "correoContacto": "nuevo@partido.bo",
      "fechaFundacion": "2020-06-01T00:00:00",
      "estado": true
    },
    "estadoCandidatura": {
      "id": 1,
      "estadoCandidatura": "Activo"
    },
    "procesoElectoral": {
      "nombreProceso": "EleccionesPresid",
      "descripcionProceso": "Elecciones para elegir al presidente del pais",
      "fechaInicio": "2024-01-01T00:00:00",
      "fechaFin": "2024-01-31T23:59:59",
      "cantidadVotos": 0,
      "cantidadCandidatos": 9
    }
  },
  "status": 200
}
```

### Respuesta Fallida

```json
{
  "success": false,
  "message": "Ya existe una candidatura con los mismos datos.",
  "data": null,
  "status": 200
}
```

---

## Obtener todas las candidaturas

**Método:** `GET`  
**Endpoint:** `{{host}}/api/administrador/candidaturas`  
**Estado:** ✅ OK

### Respuesta

```json
{
  "success": true,
  "message": "Todas las candidaturas obtenidas.",
  "data": [
    {
      "nombreCandidatura": "Candidatura 2025",
      "lema": "Por un futuro mejor",
      "candidato": {
        "id": 1,
        "nombreCandidato": "Juan Actual",
        "apPaterno": "Perez actual",
        "apMaterno": "García actual",
        "ciCandidato": "1234567890",
        "fechaNacimiento": "1990-01-01",
        "fotoUrl": "https://example.com/foto_actualizada.jpg",
        "correoCandidato": "",
        "propuesta": "Propuesta actualizada",
        "estadoCandidatoId": 1,
        "cargoId": 1
      },
      "partido": {
        "nombrePartido": "Partido Actualizado",
        "sigla": "PA",
        "lema": "Renovación y orden",
        "logoUrl": "http://nuevo.logo/url.png",
        "colorHex": "#ff6600",
        "pais": "Bolivia",
        "representanteLegal": "Ana Maria",
        "descripcion": "Cambio de imagen y estructura",
        "direccionSede": "Zona Sur #456",
        "paginaWeb": "https://partidoactualizado.bo",
        "telefonoContacto": "78541236",
        "correoContacto": "nuevo@partido.bo",
        "fechaFundacion": "2020-06-01T00:00:00",
        "estado": true
      },
      "estadoCandidatura": {
        "id": 1,
        "estadoCandidatura": "Activo"
      },
      "procesoElectoral": {
        "nombreProceso": "EleccionesPresid",
        "descripcionProceso": "Elecciones para elegir al presidente del pais",
        "fechaInicio": "2024-01-01T00:00:00",
        "fechaFin": "2024-01-31T23:59:59",
        "cantidadVotos": 0,
        "cantidadCandidatos": 2
      }
    },
    {
      "nombreCandidatura": "Candidatura 2020",
      "lema": "Por un futuro mejor",
      "candidato": {
        "id": 1,
        "nombreCandidato": "Juan Actual",
        "apPaterno": "Perez actual",
        "apMaterno": "García actual",
        "ciCandidato": "1234567890",
        "fechaNacimiento": "1990-01-01",
        "fotoUrl": "https://example.com/foto_actualizada.jpg",
        "correoCandidato": "",
        "propuesta": "Propuesta actualizada",
        "estadoCandidatoId": 1,
        "cargoId": 1
      },
      "partido": {
        "nombrePartido": "Partido Actualizado",
        "sigla": "PA",
        "lema": "Renovación y orden",
        "logoUrl": "http://nuevo.logo/url.png",
        "colorHex": "#ff6600",
        "pais": "Bolivia",
        "representanteLegal": "Ana Maria",
        "descripcion": "Cambio de imagen y estructura",
        "direccionSede": "Zona Sur #456",
        "paginaWeb": "https://partidoactualizado.bo",
        "telefonoContacto": "78541236",
        "correoContacto": "nuevo@partido.bo",
        "fechaFundacion": "2020-06-01T00:00:00",
        "estado": true
      },
      "estadoCandidatura": {
        "id": 1,
        "estadoCandidatura": "Activo"
      },
      "procesoElectoral": {
        "nombreProceso": "EleccionesPresid",
        "descripcionProceso": "Elecciones para elegir al presidente del pais",
        "fechaInicio": "2024-01-01T00:00:00",
        "fechaFin": "2024-01-31T23:59:59",
        "cantidadVotos": 0,
        "cantidadCandidatos": 2
      }
    }
  ],
  "status": 200
}
```
