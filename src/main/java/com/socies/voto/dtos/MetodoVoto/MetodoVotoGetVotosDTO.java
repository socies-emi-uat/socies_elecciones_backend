package com.socies.voto.dtos.MetodoVoto;

import java.util.List;

import com.socies.voto.models.MetodoVoto;
import com.socies.voto.models.Voto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MetodoVotoGetVotosDTO {
    private Long id;
    private String nombre;
    private List<Voto> votos;

    public MetodoVotoGetVotosDTO(MetodoVoto metodoVoto) {
        this.id = metodoVoto.getId();
        this.nombre = metodoVoto.getNombre();
        this.votos = metodoVoto.getVotos();
    }
}
