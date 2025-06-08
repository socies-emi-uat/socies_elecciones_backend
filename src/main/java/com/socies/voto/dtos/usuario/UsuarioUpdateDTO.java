package com.socies.voto.dtos.usuario;

import com.socies.voto.models.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String correo;
    private String carnet_identidad;
    private Rol rol;
}
