package com.socies.voto.dtos.EstadoCandidatos;

import com.socies.voto.models.EstadoCandidato;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoCandidatoDTO {

    private Long id;
    private String estado_candidato;

    public EstadoCandidatoDTO(EstadoCandidato estadoCandidato) {
        this.id = estadoCandidato.getId();
        this.estado_candidato = estadoCandidato.getEstadoCandidato();
    }

    public EstadoCandidatoDTO(Long id, String estado_candidato) {
        this.id = id;
        this.estado_candidato = estado_candidato;
    }
}
