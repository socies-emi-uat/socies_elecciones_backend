package com.socies.voto.dtos.Provincia;

import com.socies.voto.models.Departamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProvinciaDTO {
    private Long id;
    private String nombre;

    public ProvinciaDTO(Departamento departamento) {
        this.id = departamento.getId();
        this.nombre = departamento.getNombre();
    }
}
