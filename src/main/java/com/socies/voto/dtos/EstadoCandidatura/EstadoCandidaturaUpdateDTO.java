// En com.socies.voto.dtos.EstadoProceso
package com.socies.voto.dtos.EstadoCandidatura;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class EstadoCandidaturaUpdateDTO {
    @NonNull private String estadoCandidatura;

    public EstadoCandidaturaUpdateDTO(String estadoCandidatura) {
        this.estadoCandidatura = estadoCandidatura;
    }
}
