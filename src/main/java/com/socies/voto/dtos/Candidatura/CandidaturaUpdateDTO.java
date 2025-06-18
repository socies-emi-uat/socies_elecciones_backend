package com.socies.voto.dtos.Candidatura;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CandidaturaUpdateDTO {
    private String nombreCandidatura;
    private String lema;
    private Long candidatoId;
    private Long partidoId;
    private Long estadoCandidaturaId;
    private Long procesoElectoralId;
}
