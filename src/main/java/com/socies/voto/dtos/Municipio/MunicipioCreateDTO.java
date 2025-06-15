package com.socies.voto.dtos.Municipio;

import com.socies.voto.models.Departamento;
import com.socies.voto.models.Municipio;
import com.socies.voto.models.Provincia;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MunicipioCreateDTO {
    @NotBlank private String nombre;
    @NotBlank private Provincia provincia;

    public MunicipioCreateDTO(Municipio municipio) {
        this.nombre = municipio.getNombre();
        this.provincia = municipio.getProvincia();
    }
}
