package com.socies.voto.dtos.Candidatura;

import com.socies.voto.dtos.Candidato.CandidatoPublicDTO;
import com.socies.voto.dtos.Partido.PartidoPublicDTO;
import com.socies.voto.models.Candidatura;
import lombok.Getter;

@Getter
public class VCandidaturaPublicDTO {
    private String nombreCandidatura;
    private String lema;
    private PartidoPublicDTO partido;
    private CandidatoPublicDTO candidato;

    public VCandidaturaPublicDTO(Candidatura c, String foto_candidato, String foto_partido) {
        this.nombreCandidatura = c.getNombreCandidatura();
        this.lema = c.getLema();
        this.partido = new PartidoPublicDTO(c.getPartido(), null, foto_partido); // sin candidaturas
        this.candidato = new CandidatoPublicDTO(c.getCandidato(), foto_candidato);
    }

    public VCandidaturaPublicDTO(Candidatura c) {
        this.nombreCandidatura = c.getNombreCandidatura();
        this.lema = c.getLema();
        this.partido = new PartidoPublicDTO(c.getPartido(), null, null); // sin candidaturas
        this.candidato = new CandidatoPublicDTO(c.getCandidato(), null);
    }
}
