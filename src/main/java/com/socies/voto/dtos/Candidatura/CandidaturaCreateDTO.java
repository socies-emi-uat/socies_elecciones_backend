package com.socies.voto.dtos.Candidatura;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CandidaturaCreateDTO {

    private String nombreCandidatura;

    private String lema;

    private Long candidatoId;

    private Long partidoId;

    private Long estadoCandidaturaId;

    private Long procesoElectoralId;

    public CandidaturaCreateDTO(
            String nombreCandidatura,
            String lema,
            Long candidatoId,
            Long partidoId,
            Long estadoCandidaturaId,
            Long procesoElectoralId) {
        this.nombreCandidatura = nombreCandidatura;
        this.lema = lema;
        this.candidatoId = candidatoId;
        this.partidoId = partidoId;
        this.estadoCandidaturaId = estadoCandidaturaId;
        this.procesoElectoralId = procesoElectoralId;
    }
}
