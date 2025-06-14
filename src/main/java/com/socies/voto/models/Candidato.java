package com.socies.voto.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Candidato extends BaseEntity{

    @Column(name = "nombre_candidato", nullable = false, length = 30, unique = true)
    private String nombreCandidato;

    @Column(name = "apellido_paterno", nullable = false, length = 20)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false, length = 20)
    private String apellidoMaterno;

    @Column(name = "cedula_identidad", nullable = false, length = 12, unique = true)
    private String cedulaIdentidad;

    @Column(nullable = false)
    private LocalDateTime fechaNacimiento;

    @Column(name = "foto_url")
    private String fotoUrl;

    private String correo;
    private String propuesta;

    @ManyToOne
    @JoinColumn(name = "estado_candidato_id", referencedColumnName = "id", nullable = false)
    private EstadoCandidato estadoCandidato;

    @ManyToOne
    @JoinColumn(name = "cargo_id", referencedColumnName = "id", nullable = false)
    private Cargo cargo;

    public Candidato(Candidato candidato) {
        this.nombreCandidato = candidato.getNombreCandidato();
        this.apellidoPaterno = candidato.getApellidoPaterno();
        this.apellidoMaterno = candidato.getApellidoMaterno();
        this.cedulaIdentidad = candidato.getCedulaIdentidad();
        this.fechaNacimiento = candidato.getFechaNacimiento();
        this.fotoUrl = candidato.getFotoUrl();
        this.correo = candidato.getCorreo();
        this.propuesta = candidato.getPropuesta();
        this.estadoCandidato = candidato.getEstadoCandidato();
        this.cargo = candidato.getCargo();
    }

    @OneToMany(
            mappedBy = "candidato",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)

    private List<Candidatura> candidaturas;
}
