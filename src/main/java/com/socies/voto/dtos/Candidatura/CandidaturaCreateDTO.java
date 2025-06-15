package com.socies.voto.dtos.Candidatura;

import com.socies.voto.models.*;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CandidaturaCreateDTO {

    private String nombreCandidatura;
    private String lema;
    private Candidato candidato;
    private Partido partido;
    private EstadoCandidatura estadoCandidatura;
    private ProcesoElectoral procesoElectoral;

    public CandidaturaCreateDTO(Candidatura candidatura) {
        this.nombreCandidatura = candidatura.getNombreCandidatura();
        this.lema = candidatura.getLema();
        this.candidato = candidatura.getCandidato();
        this.partido = candidatura.getPartido();
        this.estadoCandidatura = candidatura.getEstadoCandidatura();
        this.procesoElectoral = candidatura.getProcesoElectoral();
    }
}
