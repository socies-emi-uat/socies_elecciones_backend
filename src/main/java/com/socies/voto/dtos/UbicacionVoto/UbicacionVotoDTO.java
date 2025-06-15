package com.socies.voto.dtos.UbicacionVoto;

import com.socies.voto.models.UbicacionVoto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UbicacionVotoDTO {
    private Long id;
    private String nombreUbicacion;
    private String descripcionUbicacion;
    private float latitude;
    private float longitude;
    private String direccion;
    private boolean estado;
    private String municipio;

    public UbicacionVotoDTO(UbicacionVoto ubicacionVoto) {
        this.id = ubicacionVoto.getId();
        this.nombreUbicacion = ubicacionVoto.getNombreUbicacion();
        this.descripcionUbicacion = ubicacionVoto.getDescripcionUbicacion();
        this.latitude = ubicacionVoto.getLatitude();
        this.longitude = ubicacionVoto.getLongitude();
        this.direccion = ubicacionVoto.getDireccion();
        this.estado = ubicacionVoto.isEstado();
        this.municipio = ubicacionVoto.getMunicipio().getNombre();
    }
}
