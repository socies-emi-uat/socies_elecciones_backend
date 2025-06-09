package com.socies.voto.dtos.EstadoCandidatos;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class EstadoCandidatoCreateDTO {

    @NonNull
    private String estado_candidato;

    public EstadoCandidatoCreateDTO(String estado_candidato) {
        this.estado_candidato = estado_candidato;
    }
}

