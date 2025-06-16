package com.socies.voto.dtos.EstadoCandidatura;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class EstadoCandidaturaCreateDTO {

    @NonNull private String estadoCandidatura;

    public EstadoCandidaturaCreateDTO(String estadoCandidatura) {
        this.estadoCandidatura = estadoCandidatura;
    }
}
