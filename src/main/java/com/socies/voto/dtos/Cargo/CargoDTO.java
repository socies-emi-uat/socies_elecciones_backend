package com.socies.voto.dtos.Cargo;

import com.socies.voto.models.Cargo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CargoDTO {
    Long id;
    String nombre_cargo;
    String descripcion;

    public CargoDTO(Cargo cargo) {
        this.id = cargo.getId();
        this.nombre_cargo = cargo.getNombre();
        this.descripcion = cargo.getDescripcion();
    }

    public CargoDTO(Long id, String nombreCargo, String descripcion) {
        this.id = id;
        this.nombre_cargo = nombreCargo;
        this.descripcion = descripcion;
    }
}
