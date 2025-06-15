package com.socies.voto.dtos.Provincia;

import com.socies.voto.models.Departamento;
import com.socies.voto.models.Provincia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProvinciaDTO {
    private Long id;
    private String nombre;

    public ProvinciaDTO(Provincia provincia) {
        this.id = provincia.getId();
        this.nombre = provincia.getNombre();
    }
}
