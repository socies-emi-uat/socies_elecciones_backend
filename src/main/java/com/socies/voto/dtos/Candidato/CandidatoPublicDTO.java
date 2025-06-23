package com.socies.voto.dtos.Candidato;

import com.socies.voto.models.Candidato;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CandidatoPublicDTO {
    private Long id;
    private String nombreCandidato;
    private String apPaterno;
    private String apMaterno;
    private LocalDate fechaNacimiento;
    private String fotoUrl;
    private String propuesta;
    private String cargo;
    private String estado;

    public CandidatoPublicDTO(Candidato c) {
        this.id = c.getId();
        this.nombreCandidato = c.getNombreCandidato();
        this.apPaterno = c.getApellidoPaterno();
        this.apMaterno = c.getApellidoMaterno();
        this.fechaNacimiento = c.getFechaNacimiento();
        this.fotoUrl = c.getFotoUrl();
        this.propuesta = c.getPropuesta();
        this.cargo = c.getCargo() != null ? c.getCargo().getNombre() : "Sin cargo";
        this.estado =
                c.getEstadoCandidato() != null
                        ? c.getEstadoCandidato().getEstadoCandidato()
                        : "Desconocido";
    }
}
