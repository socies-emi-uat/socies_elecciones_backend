package com.socies.voto.dtos.ProceoElectoral;

import com.socies.voto.models.EstadoProceso;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class ProcesoElectoralUpdateDTO {

    String nombreProceso;
    String descripcionProceso;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    EstadoProceso estadoProceso;

    public ProcesoElectoralUpdateDTO(String nombreProceso, String descripcionProceso, LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoProceso estadoProceso) {
       this.nombreProceso =  nombreProceso;
       this.descripcionProceso =  descripcionProceso;
       this.fechaInicio = fechaInicio;
       this.fechaFin = fechaFin;
       this.estadoProceso = estadoProceso;
    }
}
