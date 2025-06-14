package com.socies.voto.dtos.ProceoElectoral;

import com.socies.voto.models.EstadoProceso;
import com.socies.voto.models.ProcesoElectoral;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProcesoElectoralCreateDTO {

    @NotBlank(message = "El nombre del proceso es obligatorio.")
    String nombreProceso;

    @NotBlank(message = "La descripcion del proceso es obligatorio.")
    String descripcionProceso;

    @NotBlank(message = "La fecha de inicio no puede estar vacia")
    LocalDateTime fechaInicio;

    @NotBlank(message = "La fecha de fin no puede estar vacia")
    LocalDateTime fechaFin;

    @NotBlank(message = "El proceso debe tener un estado")
    EstadoProceso estadoProceso;

    public ProcesoElectoralCreateDTO(ProcesoElectoral procesoElectoral) {
        this.nombreProceso = procesoElectoral.getNombreProceso();
        this.descripcionProceso = procesoElectoral.getDescripcionProceso();
        this.fechaInicio = procesoElectoral.getFechaInicio();
        this.fechaFin = procesoElectoral.getFechaFin();
        this.estadoProceso = procesoElectoral.getEstadoProceso();
    }
}
