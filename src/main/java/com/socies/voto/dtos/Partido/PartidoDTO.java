package com.socies.voto.dtos.Partido;

import com.socies.voto.models.Partido;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PartidoDTO {

    private String nombrePartido;
    private String sigla;
    private String lema;
    private String logoUrl;
    private String colorHex;
    private String pais = "Bolivia";
    private String representanteLegal;
    private String descripcion;
    private String direccionSede;
    private String paginaWeb;
    private String telefonoContacto;
    private String correoContacto;
    private LocalDateTime fechaFundacion;
    private boolean estado;

    public PartidoDTO(Partido partido) {
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
}
