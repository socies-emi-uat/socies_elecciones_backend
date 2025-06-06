package com.socies.voto.dtos.EstadoProceso;

import com.socies.voto.models.EstadoProceso;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstadoProcesoDTO {
    Long id;
    String estado_proceso;

    public EstadoProcesoDTO(EstadoProceso estado) {
        this.id = estado.getId();
        this.estado_proceso = estado.getEstado_proceso();
    }

    public EstadoProcesoDTO(Long id, String estadoProceso) {
        this.id = id;
        this.estado_proceso = estadoProceso;
    }
}
