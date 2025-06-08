package com.socies.voto.dtos.rol;

import com.socies.voto.models.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolDTO {
  private Long id;
  private String nombre;

  public RolDTO(Rol rol) {
    this.id = rol.getId();
    this.nombre = rol.getTipo_rol();
  }
}
