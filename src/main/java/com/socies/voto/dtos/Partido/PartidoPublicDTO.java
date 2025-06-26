package com.socies.voto.dtos.Partido;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.socies.voto.dtos.Candidatura.CandidaturaPublicDTO;
import com.socies.voto.models.Partido;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // 👈 Oculta campos null como candidaturas
@JsonPropertyOrder({
    "id",
    "nombrePartido",
    "sigla",
    "lema",
    "logoUrl",
    "colorHex",
    "pais",
    "representanteLegal",
    "descripcion",
    "direccionSede",
    "paginaWeb",
    "telefonoContacto",
    "correoContacto",
    "fechaFundacion",
    "estado",
    "candidaturas",
    "foto"
})
public class PartidoPublicDTO {
    private Long id;
    private String nombrePartido;
    private String sigla;
    private String lema;
    private String logoUrl;
    private String colorHex;
    private String pais;
    private String representanteLegal;
    private String descripcion;
    private String direccionSede;
    private String paginaWeb;
    private String telefonoContacto;
    private String correoContacto;
    private LocalDateTime fechaFundacion;
    private boolean estado;
    private List<CandidaturaPublicDTO> candidaturas;
    private String foto;

    public PartidoPublicDTO(Partido p, List<CandidaturaPublicDTO> candidaturas, String foto) {
        this.id = p.getId();
        this.nombrePartido = p.getNombrePartido();
        this.sigla = p.getSigla();
        this.lema = p.getLema();
        this.logoUrl = p.getLogoUrl();
        this.colorHex = p.getColorHex();
        this.pais = p.getPais();
        this.representanteLegal = p.getRepresentanteLegal();
        this.descripcion = p.getDescripcion();
        this.direccionSede = p.getDireccionSede();
        this.paginaWeb = p.getPaginaWeb();
        this.telefonoContacto = p.getTelefonoContacto();
        this.correoContacto = p.getCorreoContacto();
        this.fechaFundacion = p.getFechaFundacion();
        this.estado = p.isEstado();
        this.candidaturas = candidaturas;
        this.foto = foto;
    }
}
