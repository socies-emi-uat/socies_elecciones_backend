package com.socies.voto.dtos.Provincia;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProvinciaUpdateDTO {
    @NotBlank private String nombre;

    public ProvinciaUpdateDTO(@NotBlank String nombre) {
        this.nombre = nombre;
    }
}
