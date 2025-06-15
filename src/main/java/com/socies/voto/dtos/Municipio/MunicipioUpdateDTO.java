package com.socies.voto.dtos.Municipio;

import com.socies.voto.models.Departamento;
import com.socies.voto.models.Provincia;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MunicipioUpdateDTO {
    @NotBlank private String nombre;
    @NotBlank private Departamento departamento;

    public MunicipioUpdateDTO(Provincia provincia) {
        this.nombre = provincia.getNombre();
        this.departamento = provincia.getDepartamento();
    }
}
