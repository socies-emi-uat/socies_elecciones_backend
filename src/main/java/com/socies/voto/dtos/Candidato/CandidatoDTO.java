package com.socies.voto.dtos.Candidato;

import com.socies.voto.models.Candidato;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CandidatoDTO {
    Long id;
    String nombreCandidato;
    String apPaterno;
    String apMaterno;
    String ciCandidato;
    LocalDate fechaNacimiento;
    String fotoUrl;
    String correoCandidato;
    String propuesta;
    Long estadoCandidatoId;
    Long cargoId;

    // Constructor para mapear desde la entidad Candidato
    public CandidatoDTO(Candidato candidato) {
        this.id = candidato.getId();
        this.nombreCandidato = candidato.getNombreCandidato();
        this.apPaterno = candidato.getApellidoPaterno();
        this.apMaterno = candidato.getApellidoMaterno();
        this.ciCandidato = candidato.getCedulaIdentidad();
        this.fechaNacimiento = candidato.getFechaNacimiento();
        this.fotoUrl = candidato.getFotoUrl();
        this.correoCandidato = candidato.getCorreo();
        this.propuesta = candidato.getPropuesta();
        this.estadoCandidatoId = candidato.getEstadoCandidato().getId();
        this.cargoId = candidato.getCargo().getId();
    }

    // Constructor para crear un DTO con valores específicos
    public CandidatoDTO(
            Long id,
            String nombreCandidato,
            String apPaterno,
            String apMaterno,
            String ciCandidato,
            LocalDate fechaNacimiento,
            String fotoUrl,
            String correoCandidato,
            String propuesta,
            Long estadoCandidatoId,
            Long cargoId) {
        this.id = id;
        this.nombreCandidato = nombreCandidato;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.ciCandidato = ciCandidato;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoUrl = fotoUrl;
        this.correoCandidato = correoCandidato;
        this.propuesta = propuesta;
        this.estadoCandidatoId = estadoCandidatoId;
        this.cargoId = cargoId;
    }
}
