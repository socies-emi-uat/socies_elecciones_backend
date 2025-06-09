package com.socies.voto.dtos.EstadoProceso;

import com.socies.voto.models.EstadoProceso;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoProcesoDTO {
    Long id;
    String estadoProceso;

    public EstadoProcesoDTO(EstadoProceso estado) {
        this.id = estado.getId();
        this.estadoProceso = estado.getEstadoProceso();
    }

    public EstadoProcesoDTO(Long id, String estadoProceso) {
        this.id = id;
        this.estadoProceso = estadoProceso;
    }
}
