package com.socies.voto.models;

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
public class Partido extends BaseEntity {

    @Column(name = "nombre_partido", nullable = false, length = 100)
    private String nombrePartido;

    @Column(nullable = false, unique = true, length = 10)
    private String sigla;

    @Column(length = 100)
    private String lema;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(length = 10)
    private String colorHex;

    private String pais = "Bolivia";

    @Column(name = "representante_legal", length = 100)
    private String representanteLegal;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "direccion_sede", length = 150)
    private String direccionSede;

    @Column(name = "pagina_web", length = 100)
    private String paginaWeb;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "correo_contacto", length = 100)
    private String correoContacto;

    @Column(nullable = false)
    private boolean estado = true;

    @Column(name = "fecha_fundacion")
    private LocalDateTime fechaFundacion;

    public Partido(Partido partido) {
        this.nombrePartido = partido.getNombrePartido();
        this.sigla = partido.getSigla();
        this.lema = partido.getLema();
        this.logoUrl = partido.getLogoUrl();
        this.colorHex = partido.getColorHex();
        this.pais = partido.getPais();
        this.representanteLegal = partido.getRepresentanteLegal();
        this.descripcion = partido.getDescripcion();
        this.direccionSede = partido.getDireccionSede();
        this.paginaWeb = partido.getPaginaWeb();
        this.telefonoContacto = partido.getTelefonoContacto();
        this.correoContacto = partido.getCorreoContacto();
        this.fechaFundacion = partido.getFechaFundacion();
        this.estado = partido.isEstado();
    }

    @OneToMany(
            mappedBy = "partido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Candidatura> candidaturas;
}
