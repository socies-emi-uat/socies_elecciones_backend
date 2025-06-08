package com.socies.voto.dtos.Cargo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class CargoCreateDTO {

  @NonNull String nombre_cargo;
  String descripcion;

  public CargoCreateDTO(Long id, String nombreCargo, String descripcion) {
    this.nombre_cargo = nombreCargo;
    this.descripcion = descripcion;
  }
}
