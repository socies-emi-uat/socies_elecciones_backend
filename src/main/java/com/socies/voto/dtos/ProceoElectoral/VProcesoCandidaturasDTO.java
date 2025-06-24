package com.socies.voto.dtos.ProceoElectoral;

import com.socies.voto.dtos.Candidatura.VCandidaturaPublicDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VProcesoCandidaturasDTO {
    private String nombreProceso;
    private String descripcionProceso;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private List<VCandidaturaPublicDTO> candidaturas;

    public VProcesoCandidaturasDTO(
            String nombreProceso,
            String descripcionProceso,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            List<VCandidaturaPublicDTO> candidaturas) {
        this.nombreProceso = nombreProceso;
        this.descripcionProceso = descripcionProceso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.candidaturas = candidaturas;
    }
}
