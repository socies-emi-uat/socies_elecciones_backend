package com.socies.voto.dtos.MetodoVoto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class MetodoVotoCreateDTO {
    @NonNull private String nombre;

    public MetodoVotoCreateDTO(String nombre) {
        this.nombre = nombre;
    }
}
