package com.socies.voto.dtos.Candidatura;

import com.socies.voto.models.Candidato;
import com.socies.voto.models.Candidatura;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.models.Partido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CandidaturaUpdateDTO {
    private String nombreCandidatura;
    private String lema;
    private Candidato candidato;
    private Partido partido;
    private EstadoCandidatura estadoCandidatura;

    public CandidaturaUpdateDTO(Candidatura candidatura) {
        this.nombreCandidatura = candidatura.getNombreCandidatura();
        this.lema = candidatura.getLema();
        this.candidato = candidatura.getCandidato();
        this.partido = candidatura.getPartido();
        this.estadoCandidatura = candidatura.getEstadoCandidatura();
    }
}
