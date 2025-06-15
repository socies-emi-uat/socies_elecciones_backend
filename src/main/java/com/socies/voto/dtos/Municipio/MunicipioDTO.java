package com.socies.voto.dtos.Municipio;

import com.socies.voto.models.Provincia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MunicipioDTO {
    private Long id;
    private String nombre;

    public MunicipioDTO(Provincia provincia) {
        this.id = provincia.getId();
        this.nombre = provincia.getNombre();
    }
}
