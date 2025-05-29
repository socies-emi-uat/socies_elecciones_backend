package com.socies.voto.dtos.usuario;


import com.socies.voto.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioActivosDTO {

    private Long id;
    private String nombre;
    private String email;
    private String apellidos;

    private String role;

    public UsuarioActivosDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.email = usuario.getCorreo();
        this.apellidos = usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno();
        this.role = usuario.getRol().getTipo_rol();
    }
}
