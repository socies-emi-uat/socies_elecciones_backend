package com.socies.voto.dtos.usuario;

import com.socies.voto.models.Rol;
import com.socies.voto.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String cedulaIdentidad;
    private String correo;
    private boolean isDeleted;
    private boolean estado;
    private String rolNombre;

    // Constructor explícito (opcional)
    public UsuarioDTO(Long id, String nombre, String apellidoPaterno, String apellidoMaterno,
                      String cedulaIdentidad, String correo, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.cedulaIdentidad = cedulaIdentidad;
        this.correo = correo;
        this.rolNombre = rol.getTipo_rol();
    }

    // Constructor desde entidad Usuario
    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellidoPaterno = usuario.getApellidoPaterno();
        this.apellidoMaterno = usuario.getApellidoMaterno();
        this.cedulaIdentidad = usuario.getCedulaIdentidad();
        this.correo = usuario.getCorreo();
        this.isDeleted = usuario.is_deleted();
        this.estado = usuario.isEstado();
        this.rolNombre = usuario.getRol().getTipo_rol();
    }
}
