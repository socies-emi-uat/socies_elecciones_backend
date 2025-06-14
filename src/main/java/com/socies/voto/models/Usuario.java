package com.socies.voto.models;

import com.socies.voto.dtos.usuario.UsuarioCreateDTO;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String nombre;

    @Column(nullable = false, length = 45)
    private String apellidoPaterno;

    @Column(length = 45)
    private String apellidoMaterno;

    @Column(nullable = false, length = 12, unique = true)
    private String cedulaIdentidad;

    @Column(nullable = false)
    private LocalDateTime fechaNacimiento;

    @Column(length = 45, unique = true)
    private String correo;

    @Column(nullable = false)
    private boolean is_deleted = false; // usado para solft delete no borrar.

    @Column(nullable = false)
    private boolean estado = false;

    @Column(nullable = false)
    private String contrasenaHash;

    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id", nullable = false)
    private Rol rol;

    public Usuario(UsuarioCreateDTO usuarioCreateDTO, String passwordHashed, Rol rol) {
        this.nombre = usuarioCreateDTO.getNombre();
        this.apellidoPaterno = usuarioCreateDTO.getApellidoPaterno();
        this.apellidoMaterno = usuarioCreateDTO.getApellidoMaterno();
        this.cedulaIdentidad = usuarioCreateDTO.getCedulaIdentidad();
        this.fechaNacimiento = usuarioCreateDTO.getFechaNacimiento();
        this.correo = usuarioCreateDTO.getCorreo();
        this.contrasenaHash = passwordHashed;
        this.rol = rol;
    }

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Voto> votos;
}
