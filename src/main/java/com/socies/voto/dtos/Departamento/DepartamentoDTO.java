package com.socies.voto.dtos.Departamento;

import com.socies.voto.models.Departamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DepartamentoDTO {
    private Long id;
    private String nombre;

    public DepartamentoDTO(Departamento departamento) {
        this.id = departamento.getId();
        this.nombre = departamento.getNombre();
    }
}
