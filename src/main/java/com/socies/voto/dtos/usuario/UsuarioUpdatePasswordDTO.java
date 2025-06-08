package com.socies.voto.dtos.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdatePasswordDTO {

  private String oldPassword;
  private String newPassword;
}
