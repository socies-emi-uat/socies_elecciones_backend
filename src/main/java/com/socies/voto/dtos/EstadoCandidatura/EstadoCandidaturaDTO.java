package com.socies.voto.dtos.EstadoCandidatura;

import com.socies.voto.models.EstadoCandidatura;
import com.socies.voto.models.EstadoProceso;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EstadoCandidaturaDTO {
    Long id;
    String estadoCandidatura;

    public EstadoCandidaturaDTO(EstadoCandidatura estado) {
        this.id = estado.getId();
        this.estadoCandidatura = estado.getEstadoCandidatura();
    }

    public EstadoCandidaturaDTO(Long id, String estadoProceso) {
        this.id = id;
        this.estadoCandidatura = estadoProceso;
    }
}
