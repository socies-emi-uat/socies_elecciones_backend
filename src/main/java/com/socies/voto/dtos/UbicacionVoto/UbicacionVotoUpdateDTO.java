package com.socies.voto.dtos.UbicacionVoto;

import com.socies.voto.models.Municipio;
import com.socies.voto.models.UbicacionVoto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UbicacionVotoUpdateDTO {
    @NotBlank
    String nombreUbicacion;
    @NotBlank
    String descripcionUbicacion;
    float latitude;
    float longitude;
    @NotBlank
    String direccion;
    Municipio municipio;

    public UbicacionVotoUpdateDTO(UbicacionVoto ubicacionVoto) {
        this.nombreUbicacion = ubicacionVoto.getNombreUbicacion();
        this.descripcionUbicacion = ubicacionVoto.getDescripcionUbicacion();
        this.latitude = ubicacionVoto.getLatitude();
        this.longitude = ubicacionVoto.getLongitude();
        this.direccion = ubicacionVoto.getDireccion();
        this.municipio = ubicacionVoto.getMunicipio();
    }
}
