package com.socies.voto.dtos.Departamento;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepartamentoUpdateDTO {
    @NotBlank
    private String nombre;

    public DepartamentoUpdateDTO(@NotBlank String nombre) {
        this.nombre = nombre;
    }
}
