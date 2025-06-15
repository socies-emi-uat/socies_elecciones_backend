package com.socies.voto.dtos.Candidatura;

import com.socies.voto.dtos.Candidato.CandidatoDTO;
import com.socies.voto.dtos.EstadoCandidatura.EstadoCandidaturaDTO;
import com.socies.voto.dtos.Partido.PartidoDTO;
import com.socies.voto.dtos.ProceoElectoral.ProcesoElectoralDTO;
import com.socies.voto.models.Candidato;
import com.socies.voto.models.Candidatura;
import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.models.Partido;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CandidaturaDTO {
    private String nombreCandidatura;
    private String lema;
    private CandidatoDTO candidato;
    private PartidoDTO partido;
    private EstadoCandidaturaDTO estadoCandidatura;
    private ProcesoElectoralDTO procesoElectoral;

    public CandidaturaDTO(Candidatura candidatura) {
        this.nombreCandidatura = candidatura.getNombreCandidatura();
        this.lema = candidatura.getLema();
        this.candidato = new CandidatoDTO(candidatura.getCandidato());
        this.partido = new PartidoDTO(candidatura.getPartido());
        this.estadoCandidatura = new EstadoCandidaturaDTO(candidatura.getEstadoCandidatura());
        this.procesoElectoral = new ProcesoElectoralDTO(candidatura.getProcesoElectoral());
    }
}
