package com.socies.voto.dtos.EstadoProceso;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class EstadoProcesoCreateDTO {

    @NonNull
    private String estadoProceso;

    public EstadoProcesoCreateDTO(String estadoProceso) {
        this.estadoProceso = estadoProceso;
    }
}
