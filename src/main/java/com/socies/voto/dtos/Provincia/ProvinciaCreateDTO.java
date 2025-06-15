package com.socies.voto.dtos.Provincia;

import com.socies.voto.models.Departamento;
import com.socies.voto.models.Provincia;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProvinciaCreateDTO {
    @NotBlank private String nombre;
    @NotBlank private Departamento departamento;

    public ProvinciaCreateDTO(Provincia provincia) {
        this.nombre = provincia.getNombre();
        this.departamento = provincia.getDepartamento();
    }
}
