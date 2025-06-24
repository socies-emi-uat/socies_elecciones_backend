package com.socies.voto.dtos.usuario;

import com.socies.voto.models.Usuario;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class VUsuarioPerfilDTO {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String cedulaIdentidad;
    private String correo;
    private LocalDateTime fechaNacimiento;
    private boolean estado;

    public VUsuarioPerfilDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellidoPaterno = usuario.getApellidoPaterno();
        this.apellidoMaterno = usuario.getApellidoMaterno();
        this.cedulaIdentidad = usuario.getCedulaIdentidad();
        this.correo = usuario.getCorreo();
        this.fechaNacimiento = usuario.getFechaNacimiento();
        this.estado = usuario.isEstado();
    }
}
