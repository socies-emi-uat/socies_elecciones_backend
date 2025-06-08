package com.socies.voto.dtos.usuario;

import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioSimpleDTO {

  private Long id;
  private String nombre;
  private String email;
  private String apellidos;
  private String role;

  public UsuarioSimpleDTO(
      Long id, String nombre, String paterno, String materno, String email, Rol role) {
    this.id = id;
    this.nombre = nombre;
    this.email = email;
    this.apellidos = paterno + " " + materno;
    this.role = role.getTipo_rol();
  }

  public UsuarioSimpleDTO(Usuario usuario) {
    this.id = usuario.getId();
    this.nombre = usuario.getNombre();
    this.email = usuario.getCorreo();
    this.apellidos = usuario.getApellidoPaterno();
    this.role = usuario.getRol().getTipo_rol();
  }
}
