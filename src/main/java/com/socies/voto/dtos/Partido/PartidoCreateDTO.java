package com.socies.voto.dtos.Partido;

import com.socies.voto.models.Partido;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartidoCreateDTO {

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

    public PartidoCreateDTO(Partido partido) {
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
    }
}
