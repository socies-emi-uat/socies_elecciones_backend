package com.socies.voto.dtos.Municipio;

import com.socies.voto.dtos.Provincia.ProvinciaDTO;
import com.socies.voto.models.Municipio;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MunicipioDTO {
    private Long id;
    private String nombre;
    private ProvinciaDTO provincia;

    public MunicipioDTO(Municipio municipio) {
        this.id = municipio.getId();
        this.nombre = municipio.getNombre();
        this.provincia = new ProvinciaDTO(municipio.getProvincia());
    }
}
