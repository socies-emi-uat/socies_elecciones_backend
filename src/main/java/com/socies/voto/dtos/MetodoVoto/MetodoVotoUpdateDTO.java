package com.socies.voto.dtos.MetodoVoto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class MetodoVotoUpdateDTO {
    @NonNull private String nombre;

    public MetodoVotoUpdateDTO(String nombre) {
        this.nombre = nombre;
    }
}
