package com.socies.voto.dtos.ProceoElectoral;

import com.socies.voto.models.ProcesoElectoral;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcesoElectoralDTO {
    String nombreProceso;
    String descripcionProceso;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    int cantidadVotos;
    int cantidadCandidatos;

    public ProcesoElectoralDTO(ProcesoElectoral procesoElectoral) {
        this.nombreProceso = procesoElectoral.getNombreProceso();
        this.descripcionProceso = procesoElectoral.getDescripcionProceso();
        this.fechaInicio = procesoElectoral.getFechaInicio();
        this.fechaFin = procesoElectoral.getFechaFin();

        this.cantidadVotos =
                (procesoElectoral.getVotos() != null) ? procesoElectoral.getVotos().size() : 0;
        this.cantidadCandidatos =
                (procesoElectoral.getCandidaturas() != null)
                        ? procesoElectoral.getCandidaturas().size()
                        : 0;
    }
}
