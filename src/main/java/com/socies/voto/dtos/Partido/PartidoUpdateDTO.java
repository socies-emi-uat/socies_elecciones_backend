package com.socies.voto.dtos.Partido;

import com.socies.voto.models.Partido;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartidoUpdateDTO {

    @NotBlank(message = "El nombre del partido es obligatorio.")
    private String nombrePartido;

    @NotBlank(message = "La sigla del partido es obligatorio.")
    private String sigla;

    private String lema;
    private String logoUrl;
    private String colorHex;
    private String representanteLegal;
    private String descripcion;
    private String direccionSede;
    private String paginaWeb;
    private String telefonoContacto;
    private String correoContacto;
    private LocalDateTime fechaFundacion;
    private boolean estado;

    public PartidoUpdateDTO(Partido partido) {
        this.nombrePartido = partido.getNombrePartido();
        this.sigla = partido.getSigla();
        this.lema = partido.getLema();
        this.logoUrl = partido.getLogoUrl();
        this.colorHex = partido.getColorHex();
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
