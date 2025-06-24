package com.socies.voto.dtos.Candidatura;

import com.socies.voto.dtos.Candidato.CandidatoPublicDTO;
import com.socies.voto.models.Candidatura;
import lombok.Getter;

@Getter
public class CandidaturaPublicDTO {
    private String nombreCandidatura;
    private String lema;
    private CandidatoPublicDTO candidato;

    public CandidaturaPublicDTO(Candidatura c) {
        this.nombreCandidatura = c.getNombreCandidatura();
        this.lema = c.getLema();
        this.candidato = new CandidatoPublicDTO(c.getCandidato());
    }
}
