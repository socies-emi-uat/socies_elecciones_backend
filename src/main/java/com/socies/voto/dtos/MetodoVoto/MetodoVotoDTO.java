
package com.socies.voto.dtos.MetodoVoto;

import com.socies.voto.models.MetodoVoto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MetodoVotoDTO {
    private Long id;
    private String nombre;

    public MetodoVotoDTO(MetodoVoto metodoVoto) {
        this.id = metodoVoto.getId();
        this.nombre = metodoVoto.getNombre();
    }

    public MetodoVotoDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
